package com.mouse.users.iam.test;

import com.mouse.framework.test.EnableTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.*;

@Documented
@EnableTestClient
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {
}
