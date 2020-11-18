package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class PayloadTest {

    private static Stream<String> emptyStr() {
        return Stream.of(null, "");
    }

    @Test
    void should_be_able_to_build_correctly() {
        Instant iat = Instant.parse("2020-11-18T00:00:00Z");
        Instant exp = Instant.parse("2020-11-18T00:00:00Z");

        Payload payload = new Payload(iat, exp, "xxxx");

        assertThat(payload.getIat()).isEqualTo(iat);
        assertThat(payload.getExp()).isEqualTo(exp);
        assertThat(payload.getCiphertext()).isEqualTo("xxxx");
    }

    @Test
    void should_be_able_to_reject_create_with_illegal_iat() {
        Throwable throwable = catchThrowable(() -> new Payload(null, Instant.now(), "xxx"));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessage("Illegal argument [iat]");
    }

    @Test
    void should_be_able_to_reject_with_illegal_exp() {
        Instant iat = Instant.now();
        Throwable throwable = catchThrowable(() -> new Payload(iat, iat.minus(1, SECONDS), "xxx"));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessage("Illegal argument [exp]");

        Throwable nullExpThrowable = catchThrowable(() -> new Payload(iat, null, "xxx"));

        assertThat(nullExpThrowable).isNotNull();
        assertThat(nullExpThrowable).isInstanceOf(IllegalArgumentException.class);
        assertThat(nullExpThrowable).hasMessage("Illegal argument [exp]");
    }

    @ParameterizedTest
    @MethodSource("emptyStr")
    void should_be_able_to_reject_with_empty_ciphertext(String illegalCiphertext) {
        Throwable throwable = catchThrowable(() -> new Payload(Instant.now(), Instant.now(), illegalCiphertext));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessage("Illegal argument [ciphertext]");
    }
}