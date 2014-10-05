package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.SquareStateEnum;

public class Knight extends AbstractPiece {

    public Knight(CoordinatesBuffer coordinatesBuffer) {
        super(false, false, SquareStateEnum.Knight, coordinatesBuffer);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        coordinatesBuffer.resetCoordinatesBuffer();
        addSquare(x + 2, y + 1, board.width, board.height);
        addSquare(x - 2, y + 1, board.width, board.height);

        addSquare(x + 2, y - 1, board.width, board.height);
        addSquare(x - 2, y - 1, board.width, board.height);

        addSquare(x + 1, y + 2, board.width, board.height);
        addSquare(x + 1, y - 2, board.width, board.height);

        addSquare(x - 1, y + 2, board.width, board.height);
        addSquare(x - 1, y - 2, board.width, board.height);
        coordinatesBuffer.sealCoordinatesBuffer();
        return coordinatesBuffer.getCoordinatesBuffer();
    }

    @Override
    public String toString() {
        return "N";
    }

}
