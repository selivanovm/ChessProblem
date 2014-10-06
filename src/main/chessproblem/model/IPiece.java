package chessproblem.model;

import java.util.BitSet;

public interface IPiece extends ISquareState {

    public PieceTypeEnum getPieceType();
    public BitSet getGuardedSquares(int x, int y, Board board);
    public boolean isGuardsLines();
    public int getCheckPriority();
}
