package chessproblem;

import chessproblem.model.Board;
import chessproblem.model.PieceTypeEnum;

import java.util.*;
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
    private boolean isBuilt = false;

    private final int boardWidth;
    private final int boardHeight;

    public Solver(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public List<Board> solve() {
        this.isBuilt = true;
        List<PieceTypeEnum> sortedPieces =
                this.pieces.stream().sorted((p1, p2) -> p1.ordinal() - p2.ordinal()).collect(Collectors.toList());

        AtomicInteger counter = new AtomicInteger(0);
        PieceGuardedSquaresCache pieceGuardedSquaresCache = new PieceGuardedSquaresCache(boardWidth, boardHeight);
        loop(new Board((byte) boardWidth, (byte) boardHeight, sortedPieces.size()), sortedPieces, counter, pieceGuardedSquaresCache);
        executor.shutdown();
        return new LinkedList<>(results.keySet());
    }

    private void loop(Board board, List<PieceTypeEnum> piecesList, AtomicInteger counter, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        if (counter.incrementAndGet() % 1000_000 == 0) {
            System.out.println(counter.get() + " > " + results.size());
        }

        if (piecesList.isEmpty()) {
            results.putIfAbsent(board, 0);
        } else {
            PieceTypeEnum startPiece = piecesList.get(0);
            List<PieceTypeEnum> newPiecesList = new LinkedList<>(piecesList);
            newPiecesList.remove(0);
            for (int x = 0; x < board.width; x++) {
                traverseColumns(board, counter, startPiece, newPiecesList, x, pieceGuardedSquaresCache);
            }
        }
    }

    private void traverseColumns(Board board, AtomicInteger counter, PieceTypeEnum startPiece, List<PieceTypeEnum> newPiecesList,
                                 int x, PieceGuardedSquaresCache pieceGuardedSquaresCache) {
        if (!board.isVerticalLineGuarded(x)) {
            List<Future<?>> futures = new LinkedList<>();
            for (int y = 0; y < board.height; y++) {
                Board newBoard = board.putPiece(startPiece, x, y, pieceGuardedSquaresCache);
                if (newBoard != null) {
                    List<PieceTypeEnum> piecesList = new LinkedList<>(newPiecesList);

                    // to prevent thread pool starving submit tasks only for initial board state
                    if (board.isEmpty()) {
                        futures.add(executor.submit(() -> loop(newBoard, piecesList, counter, pieceGuardedSquaresCache)));
                    } else {
                        loop(newBoard, piecesList, counter, pieceGuardedSquaresCache);
                    }
                }
            }
            waitUntilAllTaskEnded(futures);
        }
    }

    private void waitUntilAllTaskEnded(List<Future<?>> futures) {
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Unable to finish subtask.", e);
            }
        }
    }

    Solver addPieces(PieceTypeEnum pieceType, int amount) {
        if (!this.isBuilt) {
            checkForRepeatedPieces(pieceType);
            for (int i = 0; i < amount; i++) {
                pieces.add(pieceType);
            }
        } else {
            throw new IllegalStateException("Solver can't be modified after 'build' has been invoked.");
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
