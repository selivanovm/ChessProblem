package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Rook extends AbstractPiece {

    public Rook() {
        super(true, false);
    }

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, Board board) {
        List<SquareCoordinates> result = new LinkedList<>();
        addFullCross(result, x, y, board.width, board.height);
        return result;
    }

    @Override
    public String toString() {
        return "R";
    }

}
