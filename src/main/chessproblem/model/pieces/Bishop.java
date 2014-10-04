package main.chessproblem.model.pieces;

import main.chessproblem.model.IPiece;
import main.chessproblem.model.SquareCoordinates;

import java.util.LinkedList;
import java.util.List;

public class Bishop implements IPiece {

    private List<SquareCoordinates> result = new LinkedList<>();

    @Override
    public List<SquareCoordinates> getAttackedSquares(int x, int y, int width, int height) {
        result.clear();
        int diagonalX = Math.max(0, x - y);
        int diagonalY = Math.max(0, y - x);

        int backDiagonalX = Math.max(0, x - (height - y - 1));
        int backDiagonalY = Math.min(height - 1, y + x);

/*
        System.out.printf("x = %d, y = %d, w = %d, h = %d, dx = %d, dy = %d | bdx = %d, bdy = %d\n",
                x, y, width, height,
                diagonalX, diagonalY, backDiagonalX, backDiagonalY);
*/
        while (diagonalX < width && diagonalY < height) {
            result.add(new SquareCoordinates(diagonalX++, diagonalY++));
        }

        while (backDiagonalX < width && backDiagonalY >= 0) {
            result.add(new SquareCoordinates(backDiagonalX++, backDiagonalY--));
        }

        return result;
    }

    @Override
    public String toString() {
        return "B";
    }
}
