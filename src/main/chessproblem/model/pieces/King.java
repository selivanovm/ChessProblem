package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.PieceTypeEnum;

import java.util.BitSet;

public class King extends AbstractPiece {

    public King(GuardedSquaresBuffer guardedSquaresBuffer) {
        super(false, false, PieceTypeEnum.King, guardedSquaresBuffer);
    }

    @Override
    public BitSet getGuardedSquares(int x, int y, Board board) {
        guardedGuardedSquaresBuffer.reset();
        for (byte i = -1; i < 2; i++) {
            addSquare(x + 1, y + i, board.width, board.height);
            addSquare(x - 1, y + i, board.width, board.height);
        }
        addSquare(x, y - 1, board.width, board.height);
        addSquare(x, y + 1, board.width, board.height);
        return guardedGuardedSquaresBuffer.get();
    }

    @Override
    public String toString() {
        return "K";
    }
}
