package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

public class Knight extends AbstractPiece {

    public Knight(byte boardWidth, byte boardHeight) {
        super(false, false, SquareStateEnum.Knight, boardWidth, boardHeight);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        resetCoordinatesBuffer();
        addSquare(x + 2, y + 1, board.width, board.height);
        addSquare(x - 2, y + 1, board.width, board.height);

        addSquare(x + 2, y - 1, board.width, board.height);
        addSquare(x - 2, y - 1, board.width, board.height);

        addSquare(x + 1, y + 2, board.width, board.height);
        addSquare(x + 1, y - 2, board.width, board.height);

        addSquare(x - 1, y + 2, board.width, board.height);
        addSquare(x - 1, y - 2, board.width, board.height);
        sealCoordinatesBuffer();
        return coordinatesBuffer.get();
    }

    @Override
    public String toString() {
        return "N";
    }

}
