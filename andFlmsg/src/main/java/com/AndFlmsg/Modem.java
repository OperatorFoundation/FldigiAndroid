package com.AndFlmsg;

public class Modem {

    public static boolean stopTX = false;
    public static boolean newAmplReady = false;

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("AndFlmsg_Modem_Interface");
    }

    // JNI native methods — modem engine
    public native static String createCModem(int modemCode);
    public native static void changeCModem(int modemCode, double newFrequency);
    public native static String initCModem(double frequency);
    public native static String rxCProcess(short[] buffer, int length);
    public native static int setSquelchLevel(double squelchLevel);
    public native static double getMetric();
    public native static double getCurrentFrequency();
    public native static int getCurrentMode();
    public native static String RsidCModemReceive(float[] myfbuffer, int length, boolean doSearch);
    public native static String createRsidModem();
    public native static int[] getModemCapListInt();
    public native static String[] getModemCapListString();
    public native static String txInit(double frequency);
    public native static boolean txCProcess(byte[] buffer, int length);
    public native static void saveEnv();
    public native static void txRSID();
    public native static int getTxProgressPercent();
    public native static void setSlowCpuFlag(boolean slowcpu);
    public native static void txPicture(byte[] txPictureBuffer, int txPictureWidth, int txPictureHeight, boolean txPictureColor);

    // C++ -> Java callbacks

    public static void txModulate(double[] outDBuffer, int length) {
    }

    public static void txToneDescriptors(int[] descriptors, int length) {
        if (toneDescriptorListener != null && !stopTX) {
            toneDescriptorListener.onToneDescriptors(descriptors, length);
        }
    }

    public static void putEchoChar(int txedChar) {
    }

    public static void updateWaterfall(double[] aFFTAmpl) {
    }

    public static void showRxViewer(int mpicW, int mpicH) {
    }

    public static void saveLastPicture() {
    }

    public static void updatePicture(int[] pictureRow, int width) {
    }

    // Listener interface for library consumers

    public interface ToneDescriptorListener {
        void onToneDescriptors(int[] descriptors, int length);
    }

    public static ToneDescriptorListener toneDescriptorListener;
}
