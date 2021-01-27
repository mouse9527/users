package com.mouse.users;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    private final MessageTranslator messageTranslator;

    public RestExceptionHandler(MessageTranslator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    @ExceptionHandler(Throwable.class)
    public Object handle(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        MessageTranslator.Message message = messageTranslator.translate(throwable, request.getLocale());
        response.setStatus(message.getStatus());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("timestamp", Instant.now());
        result.put("message", message.getMessage());
        result.put("status", message.getStatus());
        result.put("path", request.getRequestURI());
        return result;
    }
}
