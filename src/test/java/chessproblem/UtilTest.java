package chessproblem;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testIsBitSetAndSetBit() throws Exception {
        for (int i = 0; i < Integer.SIZE; i++) {
            int value = Util.setBit(0, i);
            for (int j = 0; j < Integer.SIZE; j++) {
                if (j != i) {
                    assertFalse("Bit " + j + " shouldn't be set", Util.isBitSet(value, j));
                } else {
                    assertTrue("Bit " + j + " should be set", Util.isBitSet(value, j));
                }
            }
        }
    }

    @Test
    public void testPackShortsToInt() throws Exception {
        checkShortPacking(Short.MAX_VALUE, Short.MAX_VALUE);
        checkShortPacking((short) 0, Short.MAX_VALUE);
        checkShortPacking(Short.MAX_VALUE, (short) 0);
        checkShortPacking((short) 0, (short) 0);
        checkShortPacking((short) 100, (short) 200);

        for (int i = 0; i < 10000; i++) {
            checkShortPacking((short) (Short.MAX_VALUE * Math.random()),
                    (short) (Short.MAX_VALUE * Math.random()));
        }
    }

    private void checkShortPacking(short s1, short s2) {
        int v = Util.packShortsToInt(s1, s2);
        assertEquals("First shirt should be equal to " + s1 + ". Second is " + s2, s1, Util.getFirstShortFromInt(v));
        assertEquals("Second shirt should be equal to " + s2 + ". First is " + s1, s2, Util.getSecondShortFromInt(v));
    }
}