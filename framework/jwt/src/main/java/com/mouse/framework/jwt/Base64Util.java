package com.mouse.framework.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    static byte[] encode(String signText) {
        return Base64.getEncoder().encode(signText.getBytes(StandardCharsets.UTF_8));
    }
}
