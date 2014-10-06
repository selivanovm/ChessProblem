package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.PieceTypeEnum;

import java.util.BitSet;

public class Bishop extends AbstractPiece {

    public Bishop(GuardedSquaresBuffer guardedSquaresBuffer) {
        super(false, true, PieceTypeEnum.Bishop, guardedSquaresBuffer);
    }

    @Override
    public BitSet getGuardedSquares(int x, int y, Board board) {
        guardedGuardedSquaresBuffer.reset();
        addFullDiagonalCross(x, y, board.width, board.height);
        return guardedGuardedSquaresBuffer.get();
    }

    @Override
    public String toString() {
        return "B";
    }
}
