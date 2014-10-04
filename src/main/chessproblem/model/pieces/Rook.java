package main.chessproblem.model.pieces;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Rook implements IPiece {
    List<SquareCoordinates> result = new LinkedList<>();

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        result.clear();
        for (int i = 0; i < width; i++) {
            if (y != x) {
                result.add(new SquareCoordinates(i, y));
            }
        }
        for (int j = 0; j < height; j++) {
            if (j != y) {
                result.add(new SquareCoordinates(x, j));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "R";
    }

}
