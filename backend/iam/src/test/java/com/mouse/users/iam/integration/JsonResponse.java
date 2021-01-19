package com.mouse.users.iam.integration;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {
    private final ResponseEntity<String> response;

    public JsonResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    public HttpStatus getStatusCode() {
        return response.getStatusCode();
    }

    public String strValue(String jsonPath) {
        return JsonPath.compile(jsonPath).read(response.getBody());
    }
}
