package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class GrayEncodeTest {

    @Test
    public void zeroEncodesToZero() {
        assertEquals(0, misc.grayencode(0));
    }

    @Test
    public void oneEncodesToOne() {
        assertEquals(1, misc.grayencode(1));
    }

    @Test
    public void knownGrayValues() {
        assertEquals(0, misc.grayencode(0));
        assertEquals(1, misc.grayencode(1));
        assertEquals(3, misc.grayencode(2));
        assertEquals(2, misc.grayencode(3));
        assertEquals(7, misc.grayencode(4));
        assertEquals(6, misc.grayencode(5));
        assertEquals(4, misc.grayencode(6));
        assertEquals(5, misc.grayencode(7));
    }

    @Test
    public void bijectiveWithin4Bits() {
        boolean[] seen = new boolean[16];
        for (int i = 0; i < 16; i++) {
            int g = misc.grayencode(i) & 0xF;
            assertFalse("grayencode should be injective within 4 bits, duplicate at " + i,
                    seen[g]);
            seen[g] = true;
        }
    }

    @Test
    public void allMfsk16SymbolsUnique() {
        int numtones = 16;
        boolean[] seen = new boolean[numtones];
        for (int sym = 0; sym < numtones; sym++) {
            int encoded = misc.grayencode(sym & (numtones - 1));
            assertTrue("Gray encoded value " + encoded + " should be in range [0, " + (numtones - 1) + "]",
                    encoded >= 0 && encoded < numtones);
            assertFalse("Gray encoded value " + encoded + " should be unique",
                    seen[encoded]);
            seen[encoded] = true;
        }
    }
}
