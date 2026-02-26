package com.AndFlmsg;

public class Message {

    static {
        System.loadLibrary("AndFlmsg_Flmsg_Interface");
    }

    // JNI native methods — message encoding/decoding
    public native static void saveEnv();
    public native static boolean ProcessWrapBuffer(String rawWrapBuffer);
    public native static String getUnwrapText();
    public native static String getUnwrapFilename();
    public native static String geterrtext();
    public native static String customHtmlDisplayFormat(String html, String dataPairs);
    public native static String customHtmlEditingFormat(String html, String dataPairs);
    public native static String customHtmlEditFormat(String html, String dataPairs);
    public native static String namedFile();
    public native static String createCustomBuffer(String myHeader, int reason, String dataBuffer);
    public native static String createHardCodedHeader(String myHeader, int reason, String myFormName);
    public native static String compressMaybe(String outDataBuffer);
    public native static String updateHeader(String myHeader, int reason, String headerForm);
    public native static String estimate(String jModeName, String jBufferString);
    public native static String dateTimeStamp();
    public native static String dateStamp();
    public native static String timeStamp();
    public native static String[] getArlMsgTextList();
    public native static String[] getArlHxTextList();
    public native static String cbrgcheck(String msgFieldValue, boolean stdFormat);
    public native static String gettxtrgmsg();
    public native static String escape(String stringToEscape);
    public native static String unescape(String stringToUnescape);
    public native static String expandarl(String msgToExpand);
}
