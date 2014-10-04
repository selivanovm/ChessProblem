package chessproblem.model;

import java.util.List;

public interface IPiece extends ISquareState {

    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height);

}
