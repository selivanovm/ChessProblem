package chessproblem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to find solutions for multiple unguard arrangements problem.
 * It isn't thread safe due to usage of global shared CoordinatesBuffer.
 */
public class Solver {

    private static final Logger logger = LoggerFactory.getLogger(Solver.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final List<PieceTypeEnum> pieces = new LinkedList<>();
    private boolean started = false;

    private final BoardsSet solutionsSet = new BoardsSet();

    private final int boardWidth;
    private final int boardHeight;

    public Solver(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public Result solve() {
        checkInputData();

        logger.info("Start solving: board {}x{}, pieces = [{}]", this.boardWidth, this.boardHeight, getBoardPiecesString());
        long start = System.currentTimeMillis();
        this.started = true;
        List<PieceTypeEnum> sortedPieces =
                new ArrayList<>(this.pieces.stream().sorted((p1, p2) -> p1.ordinal() - p2.ordinal()).collect(Collectors.toList()));

        AtomicInteger counter = new AtomicInteger(0);
        PieceGuardedSquaresCache pieceGuardedSquaresCache = new PieceGuardedSquaresCache(boardWidth, boardHeight);

        Board pristineBoard = new Board((byte) boardWidth, (byte) boardHeight, sortedPieces.size());
        Board[] boards = new Board[sortedPieces.size() + 1];
        boards[0] = pristineBoard;

        loop(boards, 0, pristineBoard.getCopy(), sortedPieces, counter, pieceGuardedSquaresCache);
        executor.shutdown();

        int durationSec = (int) (System.currentTimeMillis() - start) / 1000;
        logger.info("Operation took {} sec. Found {} solutions. Run {} loops.", durationSec, solutionsSet.size(), counter.get());
        return new Result(solutionsSet, counter.get());
    }

    private void checkInputData() {
        if (boardWidth < 1 || boardWidth > Board.MAX_BOARD_SIDE_SIZE) {
            throw new IllegalArgumentException(String.format("Board width should be in range 1-%d.", Board.MAX_BOARD_SIDE_SIZE));
        }
        if (boardHeight < 1 || boardHeight > Board.MAX_BOARD_SIDE_SIZE) {
            throw new IllegalArgumentException(String.format("Board height should be in range 1-%d.", Board.MAX_BOARD_SIDE_SIZE));
        }

        if (pieces.isEmpty()) {
            throw new IllegalArgumentException("Pieces count should be positive.");
        }

        if (pieces.size() >= boardWidth * boardHeight) {
            throw new IllegalArgumentException("Pieces count should be less than board size.");
        }
    }

    private String getBoardPiecesString() {
        return pieces.stream().map(PieceTypeEnum::name).reduce("", (acc, b) -> {
            if (acc.isEmpty()) {
                return b;
            } else {
                return acc + ", " + b;
            }
        });
    }

    /**
     * Loop takes piece from the list, which contains all pieces in the decreasing order of their power, and tries to
     * put it on each square of the board, for each square and piece it recursively tries to put on the board
     * next piece from list until it gets solution or position where piece is on the guarded square.
     */
    private void loop(Board[] boards, int pieceNumber, Board tmpBoard, List<PieceTypeEnum> piecesList, AtomicInteger counter, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        counter.incrementAndGet();

        int loopsCount = counter.get();
        if (loopsCount % 1000000 == 0) {
            logger.info("Loops count = {}. Solutions count = {}", loopsCount, solutionsSet.size());
        }

        Board board = boards[pieceNumber];
        if (pieceNumber == piecesList.size()) {
            solutionsSet.addBoard(board);
        } else {
            for (int x = 0; x < board.width; x++) {
                traverseRows(boards, pieceNumber, tmpBoard, counter, piecesList, x, pieceGuardedSquaresCache);
            }
        }
    }

    private void traverseRows(Board[] boards, int pieceNumber, Board tmpBoard, AtomicInteger counter, List<PieceTypeEnum> piecesList,
                              int x, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        Board board = boards[pieceNumber];

        List<Future<?>> futures = board.isEmpty() ? new LinkedList<>() : null;
        if (!board.isVerticalLineGuarded(x)) {
            for (int y = 0; y < board.height; y++) {
                tmpBoard.resetTo(board);
                Board newBoard = board.putPiece(tmpBoard, piecesList.get(pieceNumber), x, y, pieceGuardedSquaresCache);
                if (newBoard != null) {
                    // to prevent thread pool starving submit tasks only for initial board state
                    if (futures != null) {
                        final Board newBoardClone = newBoard.getCopy();
                        final Board tmpBoardClone = tmpBoard.getCopy();
                        final Board[] newBoards = new Board[boards.length];
                        newBoards[0] = board;
                        newBoards[1] = newBoardClone;
                        futures.add(executor.submit(() -> loop(newBoards, pieceNumber + 1, tmpBoardClone, piecesList, counter, pieceGuardedSquaresCache)));
                    } else {
                        Board newBoardClone = boards[pieceNumber + 1];
                        if (newBoardClone == null) {
                            newBoardClone = newBoard.getCopy();
                        }
                        newBoardClone.resetTo(newBoard);
                        boards[pieceNumber + 1] = newBoardClone;
                        loop(boards, pieceNumber + 1, tmpBoard, piecesList, counter, pieceGuardedSquaresCache);
                    }
                }
            }
        }
        waitUntilAllTaskEnded(futures);
    }

    private void waitUntilAllTaskEnded(List<Future<?>> futures) {
        if (futures != null) {
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Unable to finish subtask.", e);
                }
            }
        }
    }

    Solver addPieces(PieceTypeEnum pieceType, int amount) {
        if (!this.started) {
            checkForRepeatedPieces(pieceType);
            for (int i = 0; i < amount; i++) {
                pieces.add(pieceType);
            }
        } else {
            throw new IllegalStateException("Solver can't be modified after 'solve' has been invoked.");
        }
        return this;
    }

    private void checkForRepeatedPieces(PieceTypeEnum pieceType) {
        Stream<PieceTypeEnum> existentPiecesOfSameType = pieces.stream().filter((p -> p == pieceType));
        if (existentPiecesOfSameType.iterator().hasNext()) {
            throw new RuntimeException("Pieces of type '" + pieceType.name() + "' are already added.");
        }
    }

}
