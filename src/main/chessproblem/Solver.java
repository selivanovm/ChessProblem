package chessproblem;

import chessproblem.model.Board;
import chessproblem.model.IPiece;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class is used to find solutions for multiple unguard arrangements problem.
 * It isn't thread safe due to usage of global shared CoordinatesBuffer.
 */
public class Solver {

    private static final int THREAD_POOL_SIZE = 8;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final Map<Board, Integer> results = new ConcurrentHashMap<>();

    public List<Board> solve(List<IPiece> pieces, int width, int height) {
        AtomicInteger counter = new AtomicInteger(0);
        loop(new Board((byte) width, (byte) height), pieces, counter);
        executor.shutdown();
        return new LinkedList<>(results.keySet());
    }

    private void loop(Board board, List<IPiece> piecesList, AtomicInteger counter) {
        if (counter.incrementAndGet() % 1000_000 == 0) {
            System.out.println(counter.get() + " > " + results.size());
        }

        if (piecesList.isEmpty()) {
            results.putIfAbsent(board, 0);
        } else {
            IPiece startPiece = piecesList.get(0);
            List<IPiece> newPiecesList = piecesList.stream().filter(p -> p != startPiece).collect(Collectors.toList());
            for (int x = 0; x < board.width; x++) {
                traverseColumns(board, counter, startPiece, newPiecesList, x);
            }
        }
    }

    private void traverseColumns(Board board, AtomicInteger counter, IPiece startPiece, List<IPiece> newPiecesList, int x) {
        if (!board.isVerticalLineGuarded(x)) {
            List<Future<?>> futures = new LinkedList<>();
            for (int y = 0; y < board.height; y++) {
                if (!(board.isHorizontalLineGuarded(y) || board.isDiagonalGuarded(x, y) || board.isBackDiagonalGuarded(x, y))) {
                    Board newBoard = board.putPiece(startPiece, x, y);
                    if (newBoard != null) {
                        LinkedList<IPiece> piecesList = new LinkedList<>(newPiecesList);

                        // to prevent thread pool starving submit tasks only for initial board state
                        boolean emptyBoard = board.getPiecesOnBoard() == 0;
                        if (emptyBoard) {
                            futures.add(executor.submit(() -> loop(newBoard, piecesList, counter)));
                        } else {
                            loop(newBoard, piecesList, counter);
                        }
                    }
                }
            }
            for (Future<?> f : futures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Unable to finish subtask.", e);
                }
            }
        }
    }
}
