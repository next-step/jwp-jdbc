package core.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnderscoreConverterTest {

    @Test
    void convertToUnderscore() throws Exception {
        String camelCase = "camelCaseS";

        String result = UnderscoreConverter.convertToUnderscore(camelCase);

        assertThat(result).isEqualTo("camel_case_s");
    }

    @DisplayName("빈 문자열 일때")
    @Test
    void convertToUnderscore1() throws Exception {
        String blankString = " ";

        assertThatThrownBy(() -> UnderscoreConverter.convertToUnderscore(blankString))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 문자열 입니다");
    }

    @DisplayName("한글자 일때")
    @Test
    void convertToUnderscore2() throws Exception {
        String one = "A";

        String result = UnderscoreConverter.convertToUnderscore(one);

        assertThat(result).isEqualTo("a");
    }
}