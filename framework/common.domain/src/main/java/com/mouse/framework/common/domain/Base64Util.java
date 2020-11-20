package com.mouse.framework.common.domain;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decodeToString(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText.getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] decode(String signature) {
        return Base64.getDecoder().decode(signature);
    }
}
