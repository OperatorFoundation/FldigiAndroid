package com.AndFlmsg

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeModemTxTest {

    // The native modem uses process-wide static globals, so each test must
    // start from a known-clean state.
    @Before
    fun resetModemState() {
        Modem.toneDescriptorListener = null
        Modem.stopTX = false
    }

    @After
    fun clearListener() {
        Modem.toneDescriptorListener = null
    }

    @Test
    fun dumpModeNamesAndCodes() {
        // RSID modem must exist before the cap lists are valid.
        Modem.createRsidModem()
        val names = Modem.getModemCapListString()
        val codes = Modem.getModemCapListInt()
        val n = minOf(names.size, codes.size)
        for (i in 0 until n) {
            android.util.Log.d("ModeList", "[$i] code=${codes[i]} name='${names[i]}'")
        }
    }

    @Test
    fun getModeCodeByName_returnsMfsk16Code() {
        assertEquals(31, Modem.getModeCodeByName("MFSK16"))
    }

    @Test
    fun getModeCodeByName_unknownReturnsMinusOne() {
        assertEquals(-1, Modem.getModeCodeByName("NOT_A_MODE"))
    }


    @Test
    fun txProcess_emitsToneDescriptors() {
        var callbackCount = 0
        var totalInts = 0

        Modem.toneDescriptorListener = Modem.ToneDescriptorListener { _, length ->
            callbackCount++
            totalInts += length
        }

        assertEquals("Modem created", Modem.createCModem(Modem.getModeCodeByName("MFSK16")))
        Modem.txInit(MFSK16_CENTER_FREQ_HZ)

        val payload = "HELLO".toByteArray()
        val result = Modem.txCProcess(payload, payload.size)

        assertTrue("txCProcess should report success", result)
        assertTrue(
            "Expected at least one tone-descriptor callback, got $callbackCount",
            callbackCount > 0
        )
        assertTrue("Expected tone ints emitted, got $totalInts", totalInts > 0)
    }

    companion object {
        private const val MFSK16_CENTER_FREQ_HZ = 1500.0
    }
}