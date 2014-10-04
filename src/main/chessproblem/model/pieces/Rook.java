package chessproblem.model.pieces;

import chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Rook extends AbstractPiece {

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        List<SquareCoordinates> result = new LinkedList<>();
        addFullCross(result, x, y, width, height);
        return result;
    }

    @Override
    public String toString() {
        return "R";
    }

}
