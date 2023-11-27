package com.pixelogicmedia.delivery.utils;

public class Utilities {

    private Utilities() {
    }

    public static String humanReadableByteCount(Long bytes) {
        if (bytes == null) {
            return null;
        }
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        String units = "kMGTPE";
        int index = 0;
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            index++;
        }
        return String.format("%.1f %cB", bytes / 1000.0, units.charAt(index));
    }
}
