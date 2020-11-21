package com.mouse.framework.test;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EmbeddedMongoDBConfiguration.class)
@Documented
public @interface EnableEmbeddedMongoDB {
}
