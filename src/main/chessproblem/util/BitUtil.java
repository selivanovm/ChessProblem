package chessproblem.util;

public class BitUtil {

    public static boolean isBitSet(int x, int pos) {
        return (x & (1 << pos)) == 1;
    }

    public static int setBit(int x, int pos) {
        x |= 1 << pos;
        return x;
    }

}
