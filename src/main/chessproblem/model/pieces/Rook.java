package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.SquareStateEnum;

public class Rook extends AbstractPiece {

    public Rook(CoordinatesBuffer coordinatesBuffer) {
        super(true, false, SquareStateEnum.Rook, coordinatesBuffer);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        coordinatesBuffer.resetCoordinatesBuffer();
        addFullCross(x, y, board.width, board.height);
        coordinatesBuffer.sealCoordinatesBuffer();
        return coordinatesBuffer.getCoordinatesBuffer();
    }

    @Override
    public String toString() {
        return "R";
    }

}
