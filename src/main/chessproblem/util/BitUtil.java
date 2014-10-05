package chessproblem.util;

public class BitUtil {

    public static boolean isBitSet(int x, int pos) {
        return (x & (1 << pos)) == 1;
    }

    public static int setBit(int x, int pos) {
        x |= 1 << pos;
        return x;
    }

    public static short packBytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | b2);
    }

    public static byte getFirstByteFromShort(short s) {
        return (byte)(s >> 8);
    }

    public static byte getSecondByteFromShort(short s) {
        return (byte)(s & 0x00FF);
    }
}
