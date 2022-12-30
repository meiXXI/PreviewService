package com.meixxi.service.preview.util;

public class DimensionUtil {

    /**
     * Convert bytes in a human readable form.
     *
     * @param bytes The number of bytes.
     * @return The bytes in a human readable form.
     */
    public static String bytes2readable(long bytes) {
        String result;

        if (bytes < 1024) {
            result = bytes + " B";
        } else {
            int exp = (int) (Math.log(bytes) / Math.log(1024));

            result = String.format(
                    "%.2f %sB",
                    bytes / Math.pow(1024, exp),
                    ("KMGTPE").charAt(exp - 1)
            );
        }

        return result;
    }

}
