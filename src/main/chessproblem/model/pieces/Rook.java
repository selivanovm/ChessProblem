package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

public class Rook extends AbstractPiece {

    public Rook(byte boardWidth, byte boardHeight) {
        super(true, false, SquareStateEnum.Rook, boardWidth, boardHeight);
    }

    @Override
    public short[] getAttackedSquares(int x, int y, Board board) {
        resetCoordinatesBuffer();
        addFullCross(x, y, board.width, board.height);
        sealCoordinatesBuffer();
        return coordinatesBuffer.get();
    }

    @Override
    public String toString() {
        return "R";
    }

}
