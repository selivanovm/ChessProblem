package chessproblem.model;

import chessproblem.util.BitUtil;

/**
 * Coordinates buffer is used to keep positions guarded by piece in particular position.
 */
public class CoordinatesBuffer {
    public static final short COORDINATES_BUFFER_TERMINAL_NUMBER = BitUtil.packBytesToShort((byte) -1, (byte) -1);

    // Several threads can use coordinates buffer at the same time, so we need to
    // make it thread local.
    private static volatile ThreadLocal<short[]> coordinatesBuffer;
    private static final ThreadLocal<Integer> coordinatesBufferPosition = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public CoordinatesBuffer(int boardWidth, int boardHeight) {
        coordinatesBuffer = new ThreadLocal<short[]>() {
            @Override
            protected short[] initialValue() {
                return new short[2 * (boardWidth + boardHeight)];
            }
        };
    }

    public short[] getCoordinates() {
        return coordinatesBuffer.get();
    }

    public void resetCoordinatesBuffer() {
        coordinatesBufferPosition.set(0);
    }

    public void writeToCoordinatesBuffer(int position, short value) {
        getCoordinates()[position] = value;
        coordinatesBufferPosition.set(position + 1);
    }

    public void writeToCoordinatesBuffer(short value) {
        int pos = coordinatesBufferPosition.get();
        writeToCoordinatesBuffer(pos, value);
    }

    public void sealCoordinatesBuffer() {
        short[] buffer = getCoordinates();
        if (coordinatesBufferPosition.get() < buffer.length) {
            writeToCoordinatesBuffer(COORDINATES_BUFFER_TERMINAL_NUMBER);
        }
    }

}
