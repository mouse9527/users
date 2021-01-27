package com.mouse.users.config;

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
        int statusCode = messageTranslator.getStatusCode(throwable);
        response.setStatus(statusCode);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("timestamp", Instant.now());
        result.put("message", messageTranslator.translate(throwable));
        result.put("status", statusCode);
        result.put("path", request.getRequestURI());
        return result;
    }
}
