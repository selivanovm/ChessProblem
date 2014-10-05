package chessproblem.model;

public interface IPiece extends ISquareState {

    public short[] getAttackedSquares(int x, int y, Board board);
    public boolean isGuardsLines();
    public boolean isGuardsDiagonals();
    public int getCheckPriority();
}
