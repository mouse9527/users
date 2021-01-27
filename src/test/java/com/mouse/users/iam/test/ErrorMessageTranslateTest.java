package com.mouse.users.iam.test;

import com.mouse.framework.test.HeaderGiven;
import com.mouse.framework.test.TestClient;
import com.mouse.framework.test.TestJsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({ErrorMessageTranslateTest.TestController.class, TestClient.class})
public class ErrorMessageTranslateTest {
    @Resource
    private TestClient testClient;
    @Resource
    private HeaderGiven headerGiven;

    @Test
    void should_be_able_to_translate_error_message() {
        headerGiven.setLanguage(Locale.SIMPLIFIED_CHINESE);
        ResponseEntity<TestJsonObject> response = testClient.get("/test-message");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().strVal("$.message")).isEqualTo("用户名或密码错误！");
        assertThat(response.getBody().intVal("$.status")).isEqualTo(400);
        assertThat(response.getBody().strVal("$.path")).isEqualTo("/test-message");
        assertThat(response.getBody().has("$.timestamp")).isTrue();
    }

    @RestController
    static class TestController {

        @GetMapping("/test-message")
        public void error() {
            throw new IllegalArgumentException("error.username-password-error");
        }
    }
}
