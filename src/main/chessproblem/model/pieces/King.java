package chessproblem.model.pieces;

import chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class King extends AbstractPiece {

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        List<SquareCoordinates> result = new LinkedList<>();
        for (int i = -1; i < 2; i++) {
            addSquare(result, x + 1, y + i, width, height);
            addSquare(result, x - 1, y + i, width, height);
        }
        addSquare(result, x, y - 1, width, height);
        addSquare(result, x, y + 1, width, height);
        return result;
    }

    @Override
    public String toString() {
        return "K";
    }
}
