package chessproblem;

import chessproblem.model.Board;
import chessproblem.model.IPiece;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Solver {

    private final Set<Board> boardPrefixCache = new HashSet<>();

    public List<Board> solve(List<IPiece> pieces, int width, int height) {
        AtomicInteger counter = new AtomicInteger(0);
        loop(new Board(width, height), pieces, counter);
        return new LinkedList<>(boardPrefixCache);
    }

    private void loop(Board board, List<IPiece> piecesList, AtomicInteger counter) {
        if (counter.incrementAndGet() % 1000_000 == 0) {
            System.out.println(counter.get() + " > " + boardPrefixCache.size());
        }

        if (piecesList.isEmpty()) {
            if (!boardPrefixCache.contains(board)) {
                boardPrefixCache.add(board);
            }
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
            for (int y = 0; y < board.height; y++) {
                if (!(board.isHorizontalLineGuarded(y) || board.isDiagonalGuarded(x, y) || board.isBackDiagonalGuarded(x, y))) {
                    Board newBoard = board.putPiece(startPiece, x, y);
                    if (newBoard != null) {
                        loop(newBoard, new LinkedList<>(newPiecesList), counter);
                    }
                }
            }
        }
    }
}
