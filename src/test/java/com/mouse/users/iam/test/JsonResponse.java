package com.mouse.users.iam.test;

import com.mouse.framework.test.TestJsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse extends TestJsonObject {
    private final ResponseEntity<String> response;

    public JsonResponse(ResponseEntity<String> response) {
        super(response.getBody());
        this.response = response;
    }

    public HttpStatus getStatusCode() {
        return response.getStatusCode();
    }
}
