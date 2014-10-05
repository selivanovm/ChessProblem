package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.SquareStateEnum;

public class Queen extends AbstractPiece {

    public Queen(CoordinatesBuffer coordinatesBuffer) {
        super(true, true, SquareStateEnum.Queen, coordinatesBuffer);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        coordinatesBuffer.resetCoordinatesBuffer();
        addFullCross(x, y, board.width, board.height);
        addFullDiagonalCross(x, y, board.width, board.height);
        coordinatesBuffer.sealCoordinatesBuffer();
        return coordinatesBuffer.getCoordinatesBuffer();
    }

    @Override
    public String toString() {
        return "Q";
    }

}
