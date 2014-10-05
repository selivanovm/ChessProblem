package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

public class Queen extends AbstractPiece {

    public Queen(byte boardWidth, byte boardHeight) {
        super(true, true, SquareStateEnum.Queen, boardWidth, boardHeight);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        resetCoordinatesBuffer();
        addFullCross(x, y, board.width, board.height);
        addFullDiagonalCross(x, y, board.width, board.height);
        sealCoordinatesBuffer();
        return coordinatesBuffer.get();
    }

    @Override
    public String toString() {
        return "Q";
    }

}
