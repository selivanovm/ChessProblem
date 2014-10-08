package chessproblem;

public class Util {

    public static boolean isBitSet(int x, int pos) {
        return (x & (1 << pos)) == 1;
    }

    public static int setBit(int x, int pos) {
        x |= 1 << pos;
        return x;
    }

    public static int calcArrayPosition(int x, int y, int height) {
        return x * height + y;
    }

}
