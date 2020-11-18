package com.mouse.framework.context.core;

import com.mouse.framework.jwt.domain.Token;

import java.time.Instant;

public interface Context {
    Instant now();

    Token current();
}
