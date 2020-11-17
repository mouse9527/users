package com.mouse.users.iam.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllocationTokenTest {
    private @Resource TestRestTemplate testRestTemplate;

    @Test
    @Disabled
    void should_be_able_to_allocation_token() {
        RequestEntity<?> entity = new RequestEntity<>(HttpMethod.POST, URI.create("/tokens"));
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(entity, String.class);

        JsonResponse response = new JsonResponse(responseEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = response.strValue("$.token");
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }
}
