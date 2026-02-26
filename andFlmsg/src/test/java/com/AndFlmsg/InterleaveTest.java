package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class InterleaveTest {

    @Test
    public void mfsk16InterleaveDefaultDepth() {
        interleave ilv = new interleave(4, interleave.INTERLEAVE_FWD);
        assertEquals(10, ilv.depth);
        assertEquals(4, ilv.size);
    }

    @Test
    public void bitsOutputIsWithinRange() {
        interleave ilv = new interleave(4, interleave.INTERLEAVE_FWD);
        for (int i = 0; i < 50; i++) {
            int out = ilv.bits(i & 0xF);
            assertTrue("Interleaved bits should be 4-bit value [0..15], got " + out,
                    out >= 0 && out <= 15);
        }
    }

    @Test
    public void forwardAndReverseAreInverses() {
        int size = 4;
        int depth = 10;
        interleave fwd = new interleave(size, depth, interleave.INTERLEAVE_FWD);
        interleave rev = new interleave(size, depth, interleave.INTERLEAVE_REV);

        int[] inputSeq = {5, 10, 3, 15, 0, 7, 12, 1, 9, 6,
                          14, 2, 11, 8, 4, 13, 5, 10, 3, 15,
                          0, 7, 12, 1, 9, 6, 14, 2, 11, 8,
                          4, 13, 5, 10, 3, 15, 0, 7, 12, 1};
        int[] interleaved = new int[inputSeq.length];
        for (int i = 0; i < inputSeq.length; i++) {
            interleaved[i] = fwd.bits(inputSeq[i]);
        }
        int[] deinterleaved = new int[inputSeq.length];
        for (int i = 0; i < interleaved.length; i++) {
            deinterleaved[i] = rev.bits(interleaved[i]);
        }
        int pipelineDelay = size * depth;
        for (int i = pipelineDelay; i < inputSeq.length; i++) {
            assertEquals("Forward then reverse should recover input after pipeline delay at index " + i,
                    inputSeq[i - pipelineDelay], deinterleaved[i]);
        }
    }

    @Test
    public void deterministicOutput() {
        interleave ilv1 = new interleave(4, interleave.INTERLEAVE_FWD);
        interleave ilv2 = new interleave(4, interleave.INTERLEAVE_FWD);
        for (int i = 0; i < 30; i++) {
            int val = (i * 7) & 0xF;
            assertEquals("Same input should produce same output at step " + i,
                    ilv1.bits(val), ilv2.bits(val));
        }
    }
}
