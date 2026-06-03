package com.AndFlmsg;

/**
 * MFSK modes that FldigiAndroid represents as tone descriptors (frequency +
 * duration pairs) rather than synthesized audio, resolved via
 * {@link Modem#getModeCodeByName(String)}.
 *
 * Note: MFSK data transmit in this library currently emits tone descriptors
 * INSTEAD OF audio; restoring MFSK audio alongside tone emission is a possible future
 * change. Non-MFSK modes (PSK, Thor, Olivia, etc.) transmit as audio, do not
 * emit tone descriptors, and are intentionally not listed here.
 *
 * To add a mode: add an entry here AND a matching case in the native
 * getModeCodeByName. The toneModesResolveToValidCodes test fails if they disagree.
 */
public enum ToneMode {
    MFSK8("MFSK8"),
    MFSK16("MFSK16"),
    MFSK32("MFSK32"),
    MFSK64("MFSK64"),
    MFSK128("MFSK128"),
    MFSK64L("MFSK64L"),
    MFSK128L("MFSK128L");

    private final String fldigiName;

    ToneMode(String fldigiName) {
        this.fldigiName = fldigiName;
    }

    /** Native globals.h enum code for this mode, or -1 if unsupported. */
    public int code() {
        return Modem.getModeCodeByName(fldigiName);
    }
}
