package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class ViterbiEncoderTest {

    private static final int K = 7;
    private static final int POLY1 = 0x6d;
    private static final int POLY2 = 0x4f;

    @Test
    public void outputIsTwoBitsWide() {
        ViterbiEncoder enc = new ViterbiEncoder(K, POLY1, POLY2);
        for (int i = 0; i < 100; i++) {
            int out = enc.encode(i % 2);
            assertTrue("Output should be 0-3, got " + out, out >= 0 && out <= 3);
        }
    }

    @Test
    public void allZeroInputProducesDeterministicOutput() {
        ViterbiEncoder enc = new ViterbiEncoder(K, POLY1, POLY2);
        int[] expected = new int[10];
        for (int i = 0; i < 10; i++) {
            expected[i] = enc.encode(0);
        }
        ViterbiEncoder enc2 = new ViterbiEncoder(K, POLY1, POLY2);
        for (int i = 0; i < 10; i++) {
            assertEquals("Deterministic output for zero input at step " + i,
                    expected[i], enc2.encode(0));
        }
    }

    @Test
    public void differentInputProducesDifferentOutput() {
        ViterbiEncoder enc1 = new ViterbiEncoder(K, POLY1, POLY2);
        ViterbiEncoder enc2 = new ViterbiEncoder(K, POLY1, POLY2);
        enc1.encode(0);
        enc2.encode(1);
        int out1 = enc1.encode(0);
        int out2 = enc2.encode(0);
        assertNotEquals("Different input history should produce different output", out1, out2);
    }

    @Test
    public void freshEncoderStartsFromZeroState() {
        ViterbiEncoder enc = new ViterbiEncoder(K, POLY1, POLY2);
        int out = enc.encode(0);
        assertEquals("First zero bit through zero state should produce 0", 0, out);
    }

    @Test
    public void rateOneHalfProducesTwoBitsPerInputBit() {
        ViterbiEncoder enc = new ViterbiEncoder(K, POLY1, POLY2);
        int[] testBits = {1, 0, 1, 1, 0};
        for (int bit : testBits) {
            int out = enc.encode(bit);
            assertTrue("Each call produces a 2-bit output (0-3), got " + out,
                    out >= 0 && out <= 3);
        }
    }
}
