package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

public class King extends AbstractPiece {

    public King(byte boardWidth, byte boardHeight) {
        super(false, false, SquareStateEnum.King, boardWidth, boardHeight);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        resetCoordinatesBuffer();
        for (byte i = -1; i < 2; i++) {
            addSquare(x + 1, y + i, board.width, board.height);
            addSquare(x - 1, y + i, board.width, board.height);
        }
        addSquare(x, y - 1, board.width, board.height);
        addSquare(x, y + 1, board.width, board.height);
        sealCoordinatesBuffer();
        return coordinatesBuffer.get();
    }

    @Override
    public String toString() {
        return "K";
    }
}
