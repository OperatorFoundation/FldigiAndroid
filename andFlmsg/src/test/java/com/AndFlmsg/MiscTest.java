package com.AndFlmsg;

import org.junit.Test;
import static org.junit.Assert.*;

public class MiscTest {

    @Test
    public void decayAvgWeightOne() {
        assertEquals(42.0, misc.decayavg(100.0, 42.0, 1.0), 0.001);
        assertEquals(42.0, misc.decayavg(100.0, 42.0, 0.5), 0.001);
    }

    @Test
    public void decayAvgWeightTwo() {
        double result = misc.decayavg(100.0, 50.0, 2.0);
        assertEquals(75.0, result, 0.001);
    }

    @Test
    public void decayAvgConverges() {
        double avg = 0.0;
        for (int i = 0; i < 1000; i++) {
            avg = misc.decayavg(avg, 100.0, 10.0);
        }
        assertEquals(100.0, avg, 0.01);
    }

    @Test
    public void memsetInt() {
        int[] arr = new int[100];
        misc.memset(arr, 42);
        for (int v : arr) {
            assertEquals(42, v);
        }
    }

    @Test
    public void memsetShort() {
        short[] arr = new short[100];
        misc.memset(arr, (short) 7);
        for (short v : arr) {
            assertEquals(7, v);
        }
    }

    @Test
    public void memsetDouble() {
        double[] arr = new double[100];
        misc.memset(arr, 3.14);
        for (double v : arr) {
            assertEquals(3.14, v, 0.0);
        }
    }

    @Test
    public void memsetFloat() {
        float[] arr = new float[100];
        misc.memset(arr, 2.5f);
        for (float v : arr) {
            assertEquals(2.5f, v, 0.0f);
        }
    }

    @Test
    public void memsetEmptyArray() {
        int[] arr = new int[0];
        misc.memset(arr, 99);
        assertEquals(0, arr.length);
    }

    @Test
    public void memsetSingleElement() {
        int[] arr = new int[1];
        misc.memset(arr, 55);
        assertEquals(55, arr[0]);
    }
}
