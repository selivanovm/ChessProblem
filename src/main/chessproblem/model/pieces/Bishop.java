package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.SquareStateEnum;

public class Bishop extends AbstractPiece {

    public Bishop(CoordinatesBuffer coordinatesBuffer) {
        super(false, true, SquareStateEnum.Bishop, coordinatesBuffer);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        coordinatesBuffer.resetCoordinatesBuffer();
        addFullDiagonalCross(x, y, board.width, board.height);
        coordinatesBuffer.sealCoordinatesBuffer();
        return coordinatesBuffer.getCoordinatesBuffer();
    }

    @Override
    public String toString() {
        return "B";
    }
}
