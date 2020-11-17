package com.mouse.users.iam.domain;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class HeaderTest {

    @Test
    void should_be_able_to_create_with_alg_and_typ() {
        Header header = new Header("JWT", "RSA256");

        String base64Header = header.toString();
        assertThat(base64Header).isNotEmpty();
        String jsonHeader = new String(Base64.getDecoder().decode(base64Header.getBytes()));
        assertThat(JsonPath.compile("$.alg").<String>read(jsonHeader)).isEqualTo("JWT");
        assertThat(JsonPath.compile("$.typ").<String>read(jsonHeader)).isEqualTo("RSA256");
    }
}