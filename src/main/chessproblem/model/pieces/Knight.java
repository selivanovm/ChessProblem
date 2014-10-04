package chessproblem.model.pieces;

import chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Knight extends AbstractPiece {

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        List<SquareCoordinates> result = new LinkedList<>();
        addSquare(result, x + 2, y + 1, width, height);
        addSquare(result, x - 2, y + 1, width, height);

        addSquare(result, x + 2, y - 1, width, height);
        addSquare(result, x - 2, y - 1, width, height);

        addSquare(result, x + 1, y + 2, width, height);
        addSquare(result, x + 1, y - 2, width, height);

        addSquare(result, x - 1, y + 2, width, height);
        addSquare(result, x - 1, y - 2, width, height);

        return result;
    }

    @Override
    public String toString() {
        return "N";
    }

}
