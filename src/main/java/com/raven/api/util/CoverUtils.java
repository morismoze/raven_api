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
        return contentType.matches("image/(jpeg|jpg|png|gif|apng|tiff)");
    }

    public static boolean isValidUrl(final String coverUrl) {
        try {
            final URL url = new URL(coverUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("HEAD");
            http.connect();
            return http.getContentType().matches("image/(jpeg|png|gif|apng|tiff)");
        } catch (IOException e) {
            return false;
        }
    }
    
}
