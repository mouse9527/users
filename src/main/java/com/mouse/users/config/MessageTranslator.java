package com.mouse.users.config;

public interface MessageTranslator {
    String translate(Throwable throwable);

    int getStatusCode(Throwable throwable);
}
