package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareStateEnum;

public class Bishop extends AbstractPiece {

    public Bishop(byte boardWidth, byte boardHeight) {
        super(false, true, SquareStateEnum.Bishop, boardWidth, boardHeight);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        resetCoordinatesBuffer();
        addFullDiagonalCross(x, y, board.width, board.height);
        sealCoordinatesBuffer();
        return coordinatesBuffer.get();
    }

    @Override
    public String toString() {
        return "B";
    }
}
