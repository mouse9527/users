package com.mouse.users;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SpringMessageTranslator implements MessageTranslator {
    private final MessageSource messageSource;

    public SpringMessageTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Message translate(Throwable throwable, Locale local) {
        String message = messageSource.getMessage(throwable.getMessage(), new Object[]{}, throwable.getMessage(), local);
        return new Message(message, HttpStatus.BAD_REQUEST.value());
    }
}
