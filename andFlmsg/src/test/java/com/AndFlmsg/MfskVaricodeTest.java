package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class MfskVaricodeTest {

    @Test
    public void encodeAllPrintableAscii() {
        for (int c = 32; c < 127; c++) {
            String code = mfskVaricode.varienc(c);
            assertNotNull("Varicode for char " + c + " should not be null", code);
            assertTrue("Varicode for char " + c + " should be non-empty", code.length() > 0);
            for (char bit : code.toCharArray()) {
                assertTrue("Varicode should contain only '0' and '1', got '" + bit + "'",
                        bit == '0' || bit == '1');
            }
        }
    }

    @Test
    public void encodeDecodeRoundTrip() {
        for (int c = 0; c < 256; c++) {
            String code = mfskVaricode.varienc(c);
            assertNotNull("varienc(" + c + ") should not be null", code);
            int decoded = mfskVaricode.varidec(mfskVaricode.varidecode[c]);
            assertEquals("Round-trip encode/decode should match for char " + c, c, decoded);
        }
    }

    @Test
    public void nullCharacterHasVaricode() {
        String code = mfskVaricode.varienc(0);
        assertNotNull(code);
        assertTrue(code.length() > 0);
    }

    @Test
    public void outOfRangeFallsBackToNullChar() {
        String code256 = mfskVaricode.varienc(256);
        String code0 = mfskVaricode.varienc(0);
        assertEquals("Out of range should return varicode for char 0", code0, code256);
    }

    @Test
    public void allVaricodesUnique() {
        for (int i = 0; i < 256; i++) {
            for (int j = i + 1; j < 256; j++) {
                assertNotEquals("Varicodes for chars " + i + " and " + j + " should differ",
                        mfskVaricode.varienc(i), mfskVaricode.varienc(j));
            }
        }
    }

    @Test
    public void varidecUnknownSymbolReturnsMinusOne() {
        assertEquals(-1, mfskVaricode.varidec(0xFFFF));
    }
}
