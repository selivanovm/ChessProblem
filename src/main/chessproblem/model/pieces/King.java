package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.SquareStateEnum;

public class King extends AbstractPiece {

    public King(CoordinatesBuffer coordinatesBuffer) {
        super(false, false, SquareStateEnum.King, coordinatesBuffer);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        guardedCoordinatesBuffer.resetCoordinatesBuffer();
        for (byte i = -1; i < 2; i++) {
            addSquare(x + 1, y + i, board.width, board.height);
            addSquare(x - 1, y + i, board.width, board.height);
        }
        addSquare(x, y - 1, board.width, board.height);
        addSquare(x, y + 1, board.width, board.height);
        guardedCoordinatesBuffer.sealCoordinatesBuffer();
        return guardedCoordinatesBuffer.getCoordinates();
    }

    @Override
    public String toString() {
        return "K";
    }
}
