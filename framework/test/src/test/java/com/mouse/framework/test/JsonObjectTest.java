package com.mouse.framework.test;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonObjectTest {
    @Test
    void should_be_able_to_parse_json_string() {
        JsonObject json = new JsonObject("{\"a\": \"a\", \"b\": 1, \"c\": true, \"d\": 1.2, \"e\": {\"a\": 1}}");

        assertThat(json.strVal("$.a")).isEqualTo("a");
        assertThat(json.intVal("$.b")).isEqualTo(1);
        assertThat(json.booleanVal("$.c")).isTrue();
        assertThat(json.doubleVal("$.d")).isEqualTo(1.2);
        assertThat(json.intVal("$.e.a")).isEqualTo(1);
        assertThat(json.has("$.a")).isTrue();
        assertThat(json.has("$.g")).isFalse();
    }
}
