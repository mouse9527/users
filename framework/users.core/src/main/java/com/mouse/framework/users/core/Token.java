package com.mouse.framework.users.core;


import com.mouse.framework.common.domain.Base64Util;
import com.mouse.framework.common.domain.JsonUtil;

import java.nio.charset.StandardCharsets;

public class Token {
    private final String header;
    private final String payload;

    public Token(Header header, Payload<?> payload) {
        this.header = Base64Util.encodeToString(JsonUtil.formatToBytes(header));
        this.payload = Base64Util.encodeToString(JsonUtil.formatToBytes(payload));
    }

    public String sign(String signature) {
        return String.format("%s.%s.%s", header, payload, signature);
    }

    public byte[] getSignData() {
        return String.format("%s.%s", header, payload).getBytes(StandardCharsets.UTF_8);
    }
}
