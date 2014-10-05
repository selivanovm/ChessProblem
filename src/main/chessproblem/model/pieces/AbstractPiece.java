package chessproblem.model.pieces;

import chessproblem.model.IPiece;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;

import java.util.List;

abstract class AbstractPiece implements IPiece {

    final int checkPriority;
    final boolean guardsLines;
    final boolean guardsDiagonals;
    final SquareStateEnum squareState;

    AbstractPiece(boolean guardsLines, boolean guardsDiagonals, SquareStateEnum squareState) {
        this.guardsLines = guardsLines;
        this.guardsDiagonals = guardsDiagonals;
        this.squareState = squareState;

        // We want to start bruteforce from pieces that maximize guarded squares generation.
        // Pieces with a higher priority are checked in the first place.
        int pieceCheckPriority = 0;
        if (guardsLines) pieceCheckPriority += 2;
        if (guardsDiagonals) pieceCheckPriority += 1;
        this.checkPriority = pieceCheckPriority;
    }

    void addSquare(List<SquareCoordinates> squareCoordinates, int x, int y, int boardWidth, int boardHeight) {
        if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            squareCoordinates.add(new SquareCoordinates(x, y));
        }
    }

    void addFullCross(List<SquareCoordinates> coordinatesList, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            if (i != x) {
                coordinatesList.add(new SquareCoordinates(i, y));
            }
        }
        for (int j = 0; j < height; j++) {
            if (j != y) {
                coordinatesList.add(new SquareCoordinates(x, j));
            }
        }
    }

    void addFullDiagonalCross(List<SquareCoordinates> result, int x, int y, int width, int height) {
        int diagonalX = Math.max(0, x - y);
        int diagonalY = Math.max(0, y - x);

        int backDiagonalX = Math.max(0, x - (height - y - 1));
        int backDiagonalY = Math.min(height - 1, y + x);

        while (diagonalX < width && diagonalY < height) {
            result.add(new SquareCoordinates(diagonalX++, diagonalY++));
        }

        while (backDiagonalX < width && backDiagonalY >= 0) {
            result.add(new SquareCoordinates(backDiagonalX++, backDiagonalY--));
        }
    }

    @Override
    public boolean isGuardsLines() {
        return guardsLines;
    }

    @Override
    public boolean isGuardsDiagonals() {
        return guardsDiagonals;
    }

    @Override
    public int getCheckPriority() {
        return checkPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPiece that = (AbstractPiece) o;

        if (squareState != that.squareState) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return squareState.hashCode();
    }
}
