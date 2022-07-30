package com.raven.api.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CoverUtils {

    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static byte[] stringToBytes(final String from) {
        if (from == null) {
            return null;
        } else {
            return from.getBytes(StandardCharsets.UTF_8);
        }
    }

    public static boolean isImage(final String base64) {

        if (!base64.split(",")[0].contains("image")) {
            return false;
        }
        try {
            decoder.decode(base64);
        } catch (final IllegalArgumentException iae) {
            return false;
        }
        return true;
    }

    public static boolean isValidUrl(final String coverUrl) {

        final URL url;
        try {
            url = new URL(coverUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("HEAD");
            http.connect();
            if (!http.getContentType().startsWith("image/")) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
}
