package chessproblem;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to find solutions for multiple unguard arrangements problem.
 * It isn't thread safe due to usage of global shared CoordinatesBuffer.
 */
public class Solver {

    private static final int THREAD_POOL_SIZE = 8;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final Map<Board, Integer> results = new ConcurrentHashMap<>();
    private final List<PieceTypeEnum> pieces = new LinkedList<>();
    private boolean started = false;

    private final int boardWidth;
    private final int boardHeight;

    public Solver(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public Result solve() {
        this.started = true;
        List<PieceTypeEnum> sortedPieces =
                this.pieces.stream().sorted((p1, p2) -> p1.ordinal() - p2.ordinal()).collect(Collectors.toList());

        AtomicInteger counter = new AtomicInteger(0);
        PieceGuardedSquaresCache pieceGuardedSquaresCache = new PieceGuardedSquaresCache(boardWidth, boardHeight);
        loop(new Board((byte) boardWidth, (byte) boardHeight, sortedPieces.size()), sortedPieces, counter, pieceGuardedSquaresCache);
        executor.shutdown();
        return new Result(new LinkedList<>(results.keySet()), counter.get());
    }

    /**
     * Loop takes piece from the list, which contains all pieces in the decreasing order of their power, and tries to
     * put it on each square of the board, for each square and piece it recursively tries to put on the board
     * next piece from list until it gets solution or position where piece is on the guarded square.
     */
    private void loop(Board board, List<PieceTypeEnum> piecesList, AtomicInteger counter, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        counter.incrementAndGet();
        if (piecesList.isEmpty()) {
            results.putIfAbsent(board, 0);
        } else {
            PieceTypeEnum startPiece = piecesList.get(0);
            List<PieceTypeEnum> newPiecesList = new LinkedList<>(piecesList);
            newPiecesList.remove(0);
            for (int x = 0; x < board.width; x++) {
                traverseRows(board, counter, startPiece, newPiecesList, x, pieceGuardedSquaresCache);
            }
        }
    }

    private void traverseRows(Board board, AtomicInteger counter, PieceTypeEnum startPiece, List<PieceTypeEnum> newPiecesList,
                              int x, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        List<Future<?>> futures = board.isEmpty() ? new LinkedList<>() : null;
        if (!board.isVerticalLineGuarded(x)) {
            for (int y = 0; y < board.height; y++) {
                Board newBoard = board.putPiece(startPiece, x, y, pieceGuardedSquaresCache);
                if (newBoard != null) {
                    List<PieceTypeEnum> piecesList = new LinkedList<>(newPiecesList);

                    // to prevent thread pool starving submit tasks only for initial board state
                    if (futures != null) {
                        futures.add(executor.submit(() -> loop(newBoard, piecesList, counter, pieceGuardedSquaresCache)));
                    } else {
                        loop(newBoard, piecesList, counter, pieceGuardedSquaresCache);
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
