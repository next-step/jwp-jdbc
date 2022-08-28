package core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathPatternUtilTest {

    @DisplayName("패턴 일치 여부")
    @ParameterizedTest(name = "패턴: {0}, url: {1}: 일치여부:{2}")
    @CsvSource(value = {
        "/users/{id}, /users/1, true",
        "/users/{id}, /users, false",
        "/users, /users, true",
        "/users, /users/1, false",
        "/users/{id}, /users/{name}, true",
    })
    void is_match(final String pattern, final String path, final boolean expected) {
        final boolean actual = PathPatternUtil.isUrlMatch(pattern, path);

        assertThat(actual).isEqualTo(expected);
    }

}
