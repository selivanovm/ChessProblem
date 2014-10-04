package main.chessproblem.model.pieces;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class King implements IPiece {
    private List<SquareCoordinates> result = new LinkedList<>();
    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        result.clear();
        if (x < width - 1) {
            result.add(new SquareCoordinates(x + 1, y));
        }
        if (x > 0) {
            result.add(new SquareCoordinates(x - 1, y));
        }

        if (y < height - 1) {
            result.add(new SquareCoordinates(x, y + 1));
        }

        if (y > 0) {
            result.add(new SquareCoordinates(x, y - 1));
        }
        return result;
    }

    @Override
    public String toString() {
        return "K";
    }
}
