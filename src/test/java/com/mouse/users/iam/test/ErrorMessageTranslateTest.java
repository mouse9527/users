package com.mouse.users.iam.test;

import com.mouse.framework.test.TestClient;
import com.mouse.framework.test.TestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({ErrorMessageTranslateTest.TestController.class, TestClient.class})
public class ErrorMessageTranslateTest {
    @Resource
    private TestRestTemplate testRestTemplate;

    @Test
    void should_be_able_to_translate_error_message() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptLanguageAsLocales(Collections.singletonList(Locale.SIMPLIFIED_CHINESE));
        RequestEntity<?> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create("/test-message"));
        TestResponse response = new TestResponse(testRestTemplate.exchange(request, String.class));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
