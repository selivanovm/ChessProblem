package chessproblem.model;

import java.util.BitSet;

/**
 * This buffer is used to keep positions guarded by piece in particular position.
 */
public class GuardedSquaresBuffer {

    // Several threads can use buffer at the same time, so we need to
    // make it thread local.
    private static volatile ThreadLocal<BitSet> buffer;

    public GuardedSquaresBuffer(int boardWidth, int boardHeight) {
        buffer = new ThreadLocal<BitSet>() {
            @Override
            protected BitSet initialValue() {
                return new BitSet(boardWidth + boardHeight);
            }
        };
    }

    public BitSet get() {
        return buffer.get();
    }

    public void reset() {
        buffer.get().clear();
    }

    public void setGuardedSquare(int position) {
        get().set(position);
    }

}
