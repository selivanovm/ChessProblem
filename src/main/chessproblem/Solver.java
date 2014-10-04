package chessproblem;

import chessproblem.model.Board;
import chessproblem.model.IPiece;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Solver {

    private final Set<String> boardPrefixCache = new ConcurrentSkipListSet<>();

    public List<Board> solve(List<IPiece> pieces, int width, int height) {
        System.out.println(pieces);

        AtomicInteger counter = new AtomicInteger(0);
        List<Board> results = new LinkedList<>();
        loop(new Board(width, height), pieces, counter, results);

        System.out.println("Loop count is : " + counter.get());
        System.out.println("Solution size: " + results.size());

        for (Board b : results) {
            System.out.print(b.toString());
            System.out.println("=========");
        }
        return results;
    }

    private void loop(Board board, List<IPiece> piecesList, AtomicInteger counter, List<Board> results) {
        if (counter.incrementAndGet() % 1000_000 == 0) {
            System.out.println(counter.get() + " > " + boardPrefixCache.size());
        }

        if (piecesList.isEmpty()) {
            String positionPrefix = board.getBoardHashString();
            synchronized (boardPrefixCache) {
                if (!boardPrefixCache.contains(positionPrefix)) {
                    results.add(board);
                    boardPrefixCache.add(positionPrefix);
                }
            }
        } else {
            Set<Class<?>> pieceClasses = new HashSet<>();
            piecesList.parallelStream().forEach((startPiece) -> {
                if (!pieceClasses.contains(startPiece.getClass())) {
                    pieceClasses.add(startPiece.getClass());

                    List<IPiece> newPiecesList = piecesList.stream().filter(p -> p != startPiece).collect(Collectors.toList());
                    for (int x = 0; x < board.width; x++) {
                        for (int y = 0; y < board.height; y++) {
                            Board newBoard = board.putPiece(startPiece, x, y);
                            if (newBoard != null) {
                                loop(newBoard, new LinkedList<>(newPiecesList), counter, results);
                            }
                        }
                    }
                }
            });
        }
    }
}
