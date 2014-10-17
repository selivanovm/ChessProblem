package chessproblem;

public class Util {

    public static boolean isBitSet(int x, int pos) {
        return (x & (1 << pos)) != 0;
    }

    public static int setBit(int x, int pos) {
        x |= 1 << pos;
        return x;
    }

    public static int calcArrayPosition(int x, int y, int height) {
        return x * height + y;
    }

    /**
     * Packs two positive shorts into one int.
     */
    public static int packShortsToInt(short s1, short s2) {
        return ((s1 << 16) | s2);
    }

    /**
     * Returns positive short from the higher two bytes of int.
     */
    public static short getFirstShortFromInt(int i) {
        return (short) (i >> 16);
    }

    /**
     * Returns positive short from the lower two bytes of int.
     */
    public static short getSecondShortFromInt(int i) {
        return (short) (i & 0x0000FFFF);
    }

    public static Integer parseInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
