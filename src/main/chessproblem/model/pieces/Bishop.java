package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

import java.util.LinkedList;
import java.util.List;

public class Bishop extends AbstractPiece {

    public Bishop() {
        super(false, true, SquareStateEnum.Bishop);
    }

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, Board board) {
        List<SquareCoordinates> result = new LinkedList<>();
        addFullDiagonalCross(result, x, y, board.width, board.height);
        return result;
    }

    @Override
    public String toString() {
        return "B";
    }

}
