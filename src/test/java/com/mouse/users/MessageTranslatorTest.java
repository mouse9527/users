package com.mouse.users;

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
    @Test
    void should_be_able_to_translator_illegal_argument_exception() {
        MessageSource messageSource = mock(MessageSource.class);
        given(messageSource.getMessage(anyString(), any(), anyString(), any())).willReturn("test-message");
        MessageTranslator messageTranslator = new SpringMessageTranslator(messageSource);

        MessageTranslator.Message message = messageTranslator.translate(new IllegalArgumentException("error.test-message"), Locale.CHINA);

        assertThat(message.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(message.getMessage()).isEqualTo("test-message");
    }
}
