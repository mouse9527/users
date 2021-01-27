package com.mouse.users;

import com.mouse.users.iam.domain.UserNamePasswordErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MessageTranslatorTest {
    private MessageSource messageSource;
    private MessageTranslator messageTranslator;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        messageTranslator = new DefaultMessageTranslator(messageSource);
    }

    @Test
    void should_be_able_to_translator_illegal_argument_exception() {
        given(messageSource.getMessage(anyString(), any(), anyString(), any())).willReturn("test-message");

        MessageTranslator.Message message = messageTranslator.translate(new IllegalArgumentException("error.test-message"), Locale.SIMPLIFIED_CHINESE);

        assertThat(message.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(message.getMessage()).isEqualTo("test-message");
    }

    @Test
    void should_be_able_to_translator_illegal_status_exception() {
        MessageTranslator.Message translate = messageTranslator.translate(new IllegalStateException(), Locale.SIMPLIFIED_CHINESE);

        assertThat(translate.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void should_be_able_to_translator_other_exception() {
        MessageTranslator.Message message = messageTranslator.translate(new Exception(), Locale.SIMPLIFIED_CHINESE);

        assertThat(message.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void should_be_able_to_translator_username_password_error_exception() {
        MessageTranslator.Message message = messageTranslator.translate(new UserNamePasswordErrorException("xxx"), Locale.SIMPLIFIED_CHINESE);

        assertThat(message.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
