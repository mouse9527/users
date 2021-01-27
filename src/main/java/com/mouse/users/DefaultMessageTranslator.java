package com.mouse.users;

import com.mouse.users.iam.domain.UserNamePasswordErrorException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DefaultMessageTranslator implements MessageTranslator {
    private final MessageSource messageSource;

    public DefaultMessageTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Message translate(Throwable throwable, Locale local) {
        String message = messageSource.getMessage(throwable.getMessage(), new Object[]{}, throwable.getMessage(), local);
        return new Message(message, getStatus(throwable));
    }

    private int getStatus(Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) return HttpStatus.BAD_REQUEST.value();
        if (throwable instanceof IllegalStateException) return HttpStatus.CONFLICT.value();
        if (throwable instanceof UserNamePasswordErrorException) return HttpStatus.UNAUTHORIZED.value();
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
