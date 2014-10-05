package chessproblem.model.pieces;

import chessproblem.model.CoordinatesBuffer;
import chessproblem.model.IPiece;
import chessproblem.model.SquareStateEnum;
import chessproblem.util.BitUtil;

public abstract class AbstractPiece implements IPiece {
    final int checkPriority;
    final boolean guardsLines;
    final boolean guardsDiagonals;
    final SquareStateEnum squareState;
    final CoordinatesBuffer guardedCoordinatesBuffer;

    AbstractPiece(boolean guardsLines, boolean guardsDiagonals, SquareStateEnum squareState, CoordinatesBuffer coordinatesBuffer) {
        this.guardsLines = guardsLines;
        this.guardsDiagonals = guardsDiagonals;
        this.squareState = squareState;

        // We want to start bruteforce from pieces that maximize guarded squares generation.
        // Pieces with a higher priority are checked in the first place.
        int pieceCheckPriority = 0;
        if (guardsLines) pieceCheckPriority += 2;
        if (guardsDiagonals) pieceCheckPriority += 1;
        this.checkPriority = pieceCheckPriority;

        this.guardedCoordinatesBuffer = coordinatesBuffer;
    }

    void addSquare(int x, int y, byte boardWidth, byte boardHeight) {
        if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            guardedCoordinatesBuffer.writeToCoordinatesBuffer(BitUtil.packBytesToShort((byte) x, (byte) y));
        }
    }

    void addSquareNoChecks(byte x, byte y) {
        guardedCoordinatesBuffer.writeToCoordinatesBuffer(BitUtil.packBytesToShort(x, y));
    }

    void addFullCross(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            if (i != x) {
                addSquareNoChecks((byte) i, (byte) y);
            }
        }
        for (int j = 0; j < height; j++) {
            if (j != y) {
                addSquareNoChecks((byte) x, (byte) j);
            }
        }
    }

    void addFullDiagonalCross(int x, int y, byte width, byte height) {
        int diagonalX = Math.max(0, x - y);
        int diagonalY = Math.max(0, y - x);

        int backDiagonalX = Math.max(0, x - (height - y - 1));
        int backDiagonalY = Math.min(height - 1, y + x);

        while (diagonalX < width && diagonalY < height) {
            addSquareNoChecks((byte) (diagonalX++), (byte) (diagonalY++));
        }

        while (backDiagonalX < width && backDiagonalY >= 0) {
            addSquareNoChecks((byte) (backDiagonalX++), (byte) (backDiagonalY--));
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
