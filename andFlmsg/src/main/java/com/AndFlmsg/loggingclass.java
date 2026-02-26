package com.AndFlmsg;

public class loggingclass {

    private static String application = "";

    public loggingclass(String app) {
        application = app;
    }

    public static void writelog(String msg, Exception e, boolean b) {
        if (e == null) {
            System.err.println("[" + application + "] " + msg);
        } else {
            System.err.println("[" + application + "] " + msg);
            e.printStackTrace(System.err);
        }
    }
}
