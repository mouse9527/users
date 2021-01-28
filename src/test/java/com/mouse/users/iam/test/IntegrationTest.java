package com.mouse.users.iam.test;

import com.mouse.framework.test.EnableTestClient;
import com.mouse.framework.test.ThreadSafeHeaderMockerGiven;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Documented
@ActiveProfiles("test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableTestClient(ThreadSafeHeaderMockerGiven.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {
}
