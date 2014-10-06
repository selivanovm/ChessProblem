package chessproblem.model.pieces;

import chessproblem.model.GuardedSquaresBuffer;
import chessproblem.model.IPiece;
import chessproblem.model.PieceTypeEnum;

public abstract class AbstractPiece implements IPiece {
    final int checkPriority;
    final boolean guardsLines;
    final boolean guardsDiagonals;
    final PieceTypeEnum pieceType;
    final GuardedSquaresBuffer guardedGuardedSquaresBuffer;

    AbstractPiece(boolean guardsLines, boolean guardsDiagonals, PieceTypeEnum pieceType, GuardedSquaresBuffer guardedSquaresBuffer) {
        this.guardsLines = guardsLines;
        this.guardsDiagonals = guardsDiagonals;
        this.pieceType = pieceType;

        // We want to start bruteforce from pieces that maximize guarded squares generation.
        // Pieces with a higher priority are checked in the first place.
        int pieceCheckPriority = 0;
        if (guardsLines) pieceCheckPriority += 2;
        if (guardsDiagonals) pieceCheckPriority += 1;
        this.checkPriority = pieceCheckPriority;

        this.guardedGuardedSquaresBuffer = guardedSquaresBuffer;
    }

    @Override
    public PieceTypeEnum getPieceType() {
        return pieceType;
    }

    void addSquare(int x, int y, byte boardWidth, byte boardHeight) {
        if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            addSquareNoChecks((byte) x, (byte) y, boardHeight);
        }
    }

    void addSquareNoChecks(byte x, byte y, int boardHeight) {
        guardedGuardedSquaresBuffer.setGuardedSquare(x * boardHeight + y);
    }

    void addFullCross(int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            if (i != x) {
                addSquareNoChecks((byte) i, (byte) y, height);
            }
        }
        for (int j = 0; j < height; j++) {
            if (j != y) {
                addSquareNoChecks((byte) x, (byte) j, height);
            }
        }
    }

    void addFullDiagonalCross(int x, int y, byte width, byte height) {
        int diagonalX = Math.max(0, x - y);
        int diagonalY = Math.max(0, y - x);

        int backDiagonalX = Math.max(0, x - (height - y - 1));
        int backDiagonalY = Math.min(height - 1, y + x);

        while (diagonalX < width && diagonalY < height) {
            addSquareNoChecks((byte) (diagonalX++), (byte) (diagonalY++), height);
        }

        while (backDiagonalX < width && backDiagonalY >= 0) {
            addSquareNoChecks((byte) (backDiagonalX++), (byte) (backDiagonalY--), height);
        }
    }

    @Override
    public boolean isGuardsLines() {
        return guardsLines;
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

        if (pieceType != that.pieceType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pieceType.hashCode();
    }

}
