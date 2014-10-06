package chessproblem.model.pieces;

import chessproblem.model.Board;
import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.PieceTypeEnum;

import java.util.BitSet;

public class Knight extends AbstractPiece {

    public Knight(GuardedSquaresBuffer guardedSquaresBuffer) {
        super(false, false, PieceTypeEnum.Knight, guardedSquaresBuffer);
    }

    @Override
    public BitSet getGuardedSquares(int x, int y, Board board) {
        guardedGuardedSquaresBuffer.reset();
        addSquare(x + 2, y + 1, board.width, board.height);
        addSquare(x - 2, y + 1, board.width, board.height);

        addSquare(x + 2, y - 1, board.width, board.height);
        addSquare(x - 2, y - 1, board.width, board.height);

        addSquare(x + 1, y + 2, board.width, board.height);
        addSquare(x + 1, y - 2, board.width, board.height);

        addSquare(x - 1, y + 2, board.width, board.height);
        addSquare(x - 1, y - 2, board.width, board.height);
        return guardedGuardedSquaresBuffer.get();
    }

    @Override
    public String toString() {
        return "N";
    }

}
