package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class ToneDescriptorContractTest {

    private static final int MFSK16_NUMTONES = 16;
    private static final double MFSK16_TONESPACING = 15.625;
    private static final double MFSK16_BANDWIDTH = MFSK16_NUMTONES * MFSK16_TONESPACING;
    private static final int MFSK16_SYMLEN = 512;
    private static final double MFSK16_SAMPLERATE = 8000.0;
    private static final double TX_FREQ = 1500.0;

    private double computeToneFreq(int sym, boolean reverse) {
        double f = TX_FREQ - MFSK16_BANDWIDTH / 2;
        sym = misc.grayencode(sym & (MFSK16_NUMTONES - 1));
        if (reverse) sym = (MFSK16_NUMTONES - 1) - sym;
        return f + sym * MFSK16_TONESPACING;
    }

    @Test
    public void allToneFrequenciesWithinBandwidth() {
        double fBase = TX_FREQ - MFSK16_BANDWIDTH / 2;
        double fTop = TX_FREQ + MFSK16_BANDWIDTH / 2;
        for (int sym = 0; sym < MFSK16_NUMTONES; sym++) {
            double freq = computeToneFreq(sym, false);
            assertTrue("Tone " + sym + " freq " + freq + " should be >= " + fBase,
                    freq >= fBase - 0.001);
            assertTrue("Tone " + sym + " freq " + freq + " should be < " + fTop,
                    freq < fTop + 0.001);
        }
    }

    @Test
    public void all16TonesAreDistinctFrequencies() {
        double[] freqs = new double[MFSK16_NUMTONES];
        for (int sym = 0; sym < MFSK16_NUMTONES; sym++) {
            freqs[sym] = computeToneFreq(sym, false);
        }
        for (int i = 0; i < MFSK16_NUMTONES; i++) {
            for (int j = i + 1; j < MFSK16_NUMTONES; j++) {
                assertTrue("Tones " + i + " and " + j + " should have different frequencies",
                        Math.abs(freqs[i] - freqs[j]) > 1.0);
            }
        }
    }

    @Test
    public void toneSpacingIsCorrect() {
        double[] sortedFreqs = new double[MFSK16_NUMTONES];
        for (int sym = 0; sym < MFSK16_NUMTONES; sym++) {
            sortedFreqs[sym] = computeToneFreq(sym, false);
        }
        java.util.Arrays.sort(sortedFreqs);
        for (int i = 1; i < MFSK16_NUMTONES; i++) {
            double spacing = sortedFreqs[i] - sortedFreqs[i - 1];
            assertEquals("Spacing between sorted tones " + (i - 1) + " and " + i,
                    MFSK16_TONESPACING, spacing, 0.001);
        }
    }

    @Test
    public void symbolDurationIs64ms() {
        double durationMs = MFSK16_SYMLEN * 1000.0 / MFSK16_SAMPLERATE;
        assertEquals(64.0, durationMs, 0.001);
    }

    @Test
    public void millihertzConversionIsReversible() {
        for (int sym = 0; sym < MFSK16_NUMTONES; sym++) {
            double freq = computeToneFreq(sym, false);
            int milliHz = (int) (freq * 1000);
            double recovered = milliHz / 1000.0;
            assertEquals("Millihertz conversion round-trip for sym " + sym,
                    freq, recovered, 0.001);
        }
    }

    @Test
    public void reverseModeMirrorsFrequencies() {
        for (int sym = 0; sym < MFSK16_NUMTONES; sym++) {
            double fwd = computeToneFreq(sym, false);
            double rev = computeToneFreq(sym, true);
            double fBase = TX_FREQ - MFSK16_BANDWIDTH / 2;
            double sumOffset = (fwd - fBase) + (rev - fBase);
            double expectedSum = (MFSK16_NUMTONES - 1) * MFSK16_TONESPACING;
            assertEquals("Forward and reverse tones for sym " + sym + " should mirror around center",
                    expectedSum, sumOffset, 0.001);
        }
    }

    @Test
    public void descriptorPairsAreEvenLength() {
        int numSymbols = 10;
        int descriptorLength = numSymbols * 2;
        assertEquals(0, descriptorLength % 2);

        int[] descriptors = new int[descriptorLength];
        for (int i = 0; i < numSymbols; i++) {
            double freq = computeToneFreq(i % MFSK16_NUMTONES, false);
            descriptors[i * 2] = (int) (freq * 1000);
            descriptors[i * 2 + 1] = MFSK16_SYMLEN;
        }
        for (int i = 0; i < descriptorLength; i += 2) {
            assertTrue("Frequency millihertz should be positive", descriptors[i] > 0);
            assertEquals("Duration should be symlen", MFSK16_SYMLEN, descriptors[i + 1]);
        }
    }

    @Test
    public void fullEncodingPipelineProducesValidSymbols() {
        ViterbiEncoder enc = new ViterbiEncoder(7, 0x6d, 0x4f);
        interleave txinlv = new interleave(4, interleave.INTERLEAVE_FWD);
        int symbits = 4;

        String code = mfskVaricode.varienc('A');
        assertNotNull(code);

        int bitshreg = 0;
        int bitstate = 0;
        int symbolCount = 0;

        for (char c : code.toCharArray()) {
            int bit = c - '0';
            int data = enc.encode(bit);
            for (int i = 0; i < 2; i++) {
                bitshreg = (bitshreg << 1) | ((data >> i) & 1);
                bitstate++;
                if (bitstate == symbits) {
                    bitshreg = txinlv.bits(bitshreg);
                    assertTrue("Symbol " + bitshreg + " should be in range [0, 15]",
                            bitshreg >= 0 && bitshreg < MFSK16_NUMTONES);
                    double freq = computeToneFreq(bitshreg, false);
                    assertTrue("Tone frequency should be positive", freq > 0);
                    symbolCount++;
                    bitstate = 0;
                    bitshreg = 0;
                }
            }
        }
        assertTrue("Encoding 'A' should produce at least one symbol", symbolCount > 0);
    }
}
