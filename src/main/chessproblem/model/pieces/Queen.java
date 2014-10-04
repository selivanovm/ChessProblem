package main.chessproblem.model.pieces;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.SquareCoordinates;

import java.util.List;

public class Queen implements IPiece {
    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        return null;
    }

    @Override
    public String toString() {
        return "Q";
    }

}
