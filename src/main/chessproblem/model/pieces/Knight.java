package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Knight extends AbstractPiece {

    public Knight() {
        super(false, false);
    }

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, Board board) {
        List<SquareCoordinates> result = new LinkedList<>();
        addSquare(result, x + 2, y + 1, board.width, board.height);
        addSquare(result, x - 2, y + 1, board.width, board.height);

        addSquare(result, x + 2, y - 1, board.width, board.height);
        addSquare(result, x - 2, y - 1, board.width, board.height);

        addSquare(result, x + 1, y + 2, board.width, board.height);
        addSquare(result, x + 1, y - 2, board.width, board.height);

        addSquare(result, x - 1, y + 2, board.width, board.height);
        addSquare(result, x - 1, y - 2, board.width, board.height);

        return result;
    }

    @Override
    public String toString() {
        return "N";
    }

}
