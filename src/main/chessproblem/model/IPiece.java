package main.chessproblem.model;

import java.util.List;
import java.util.function.Function;

public interface IPiece extends ISquareState {

    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height);

}
