package com.raven.api.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CoverUtils {

    public static byte[] stringToBytes(final String from) {
        if (from == null) {
            return null;
        } else {
            return from.getBytes(StandardCharsets.UTF_8);
        }
    }

    public static boolean isImage(final String contentType) {
        if (contentType.startsWith("image/")) {
            return true; 
        }

        return false;
    }

    public static boolean isValidUrl(final String coverUrl) {
        try {
            final URL url = new URL(coverUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("HEAD");
            http.connect();
            if (!http.getContentType().startsWith("image/")) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
}
