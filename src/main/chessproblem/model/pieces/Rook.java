package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.PieceTypeEnum;

import java.util.BitSet;

public class Rook extends AbstractPiece {

    public Rook(GuardedSquaresBuffer guardedSquaresBuffer) {
        super(true, false, PieceTypeEnum.Rook, guardedSquaresBuffer);
    }

    @Override
    public BitSet getGuardedSquares(int x, int y, Board board) {
        guardedGuardedSquaresBuffer.reset();
        addFullCross(x, y, board.width, board.height);
        return guardedGuardedSquaresBuffer.get();
    }

    @Override
    public String toString() {
        return "R";
    }

}
