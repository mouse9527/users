package com.mouse.users.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SpringMessageTranslator implements MessageTranslator {
    @Override
    public String translate(Throwable throwable) {
        return "测试消息";
    }

    @Override
    public int getStatusCode(Throwable throwable) {
        return HttpStatus.BAD_REQUEST.value();
    }
}
