package chessproblem.model.pieces;

import chessproblem.model.IPiece;
import chessproblem.model.SquareCoordinates;
import chessproblem.model.SquareStateEnum;
import chessproblem.util.BitUtil;

import java.util.List;

public abstract class AbstractPiece implements IPiece {
    public static final short COORDINATES_BUFFER_TERMINAL_NUMBER = BitUtil.packBytesToShort((byte) -1, (byte) -1);

    final ThreadLocal<short[]> coordinatesBuffer = new ThreadLocal<>();
    final ThreadLocal<Integer> coordinatesBufferPosition = new ThreadLocal<>();

    final int checkPriority;
    final boolean guardsLines;
    final boolean guardsDiagonals;
    final SquareStateEnum squareState;

    AbstractPiece(boolean guardsLines, boolean guardsDiagonals, SquareStateEnum squareState, byte boardWidth, byte boardHeight) {
        this.guardsLines = guardsLines;
        this.guardsDiagonals = guardsDiagonals;
        this.squareState = squareState;

        // We want to start bruteforce from pieces that maximize guarded squares generation.
        // Pieces with a higher priority are checked in the first place.
        int pieceCheckPriority = 0;
        if (guardsLines) pieceCheckPriority += 2;
        if (guardsDiagonals) pieceCheckPriority += 1;
        this.checkPriority = pieceCheckPriority;

        this.coordinatesBuffer.set(new short[2 * (boardWidth + boardHeight)]);
        this.coordinatesBufferPosition.set(0);
    }

    void resetCoordinatesBuffer() {
        this.coordinatesBufferPosition.set(0);
    }

    void sealCoordinatesBuffer() {
        int pos = this.coordinatesBufferPosition.get();
        short[] buffer = this.coordinatesBuffer.get();
        if (pos < buffer.length) {
            writeToCoordinatesBuffer(pos, COORDINATES_BUFFER_TERMINAL_NUMBER);
        }
    }

    void addSquare(int x, int y, byte boardWidth, byte boardHeight) {
        int pos = coordinatesBufferPosition.get();
        if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
            writeToCoordinatesBuffer(pos, BitUtil.packBytesToShort((byte) x, (byte) y));
        }
    }

    void addSquareNoChecks(byte x, byte y) {
        writeToCoordinatesBuffer(BitUtil.packBytesToShort(x, y));
    }

    private void writeToCoordinatesBuffer(int position, short value) {
        coordinatesBuffer.get()[position] = value;
        coordinatesBufferPosition.set(position + 1);
    }

    private void writeToCoordinatesBuffer(short value) {
        int pos = coordinatesBufferPosition.get();
        coordinatesBuffer.get()[pos] = value;
        coordinatesBufferPosition.set(pos + 1);
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
