package com.AndFlmsg;

import java.util.HashMap;
import java.util.Map;

public class config {

    private static final Map<String, String> stringPrefs = new HashMap<>();
    private static final Map<String, Boolean> boolPrefs = new HashMap<>();

    public static String getPreferenceS(String key) {
        return stringPrefs.getOrDefault(key, "");
    }

    public static String getPreferenceS(String key, String defaultValue) {
        return stringPrefs.getOrDefault(key, defaultValue);
    }

    public static int getPreferenceI(String key, int defaultValue) {
        String val = stringPrefs.get(key);
        if (val == null || val.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double getPreferenceD(String key, double defaultValue) {
        String val = stringPrefs.get(key);
        if (val == null || val.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static boolean getPreferenceB(String key) {
        return boolPrefs.getOrDefault(key, false);
    }

    public static boolean getPreferenceB(String key, boolean defaultValue) {
        return boolPrefs.getOrDefault(key, defaultValue);
    }

    public static boolean setPreferenceS(String key, String value) {
        stringPrefs.put(key, value);
        return true;
    }

    public static boolean setPreferenceB(String key, boolean value) {
        boolPrefs.put(key, value);
        return true;
    }

    public static void putAll(Map<String, String> prefs) {
        stringPrefs.putAll(prefs);
    }

    public static void clear() {
        stringPrefs.clear();
        boolPrefs.clear();
    }
}
