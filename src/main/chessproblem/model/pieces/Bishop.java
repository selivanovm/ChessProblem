package main.chessproblem.model.pieces;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Bishop implements IPiece {

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        List<SquareCoordinates> result = new LinkedList<>();

        int diagonalX = Math.min(0, x - y);
        int diagonalY = Math.min(0, y - x);
        while (diagonalX < width && diagonalY < height) {
            result.add(new SquareCoordinates(diagonalX++, diagonalY++));
        }

        int backDiagonalX = Math.min(0, x - (height - y));
        int backDiagonalY = Math.min(height, y + x);
        while (backDiagonalX < width && backDiagonalY >= 0) {
            result.add(new SquareCoordinates(backDiagonalX++, backDiagonalY--));
        }

        return result;
    }
}
