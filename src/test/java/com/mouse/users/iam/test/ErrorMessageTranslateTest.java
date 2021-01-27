package com.mouse.users.iam.test;

import com.mouse.framework.test.TestClient;
import com.mouse.framework.test.TestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({ErrorMessageTranslateTest.TestController.class, TestClient.class})
public class ErrorMessageTranslateTest {
    @Resource
    private TestClient testClient;

    @Test
    void should_be_able_to_translate_error_message() {
        TestResponse response = testClient.get("/test-message");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().strVal("$.message")).isEqualTo("测试消息");
        assertThat(response.getBody().intVal("$.status")).isEqualTo(400);
        assertThat(response.getBody().strVal("$.path")).isEqualTo("/test-message");
        assertThat(response.getBody().has("$.timestamp")).isTrue();
    }

    @RestController
    static class TestController {

        @GetMapping("/test-message")
        public void error() {
            throw new IllegalArgumentException("error.test-message");
        }
    }
}
