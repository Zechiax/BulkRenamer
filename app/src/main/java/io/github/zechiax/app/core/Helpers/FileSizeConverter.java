package io.github.zechiax.app.core.Helpers;

import java.text.DecimalFormat;

public class FileSizeConverter {
    public static String getFormattedSizeFromBytes(long bytes) {
        // Used https://stackoverflow.com/a/5599842
        if(bytes <= 0) return "0";
        String[] units = new String[] { "B", "KB", "MB", "GB", "TB", "PB", "EB" };
        int digitGroups = (int) (Math.log10(bytes)/Math.log10(1000));
        return new DecimalFormat("#,##0.#").format(bytes/Math.pow(1000, digitGroups)) + " " + units[digitGroups];
    }
}
