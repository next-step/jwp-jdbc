package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PatternsTest {

    @DisplayName("matches 메서드는 유효한 경로를 전달하면 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/a/b/hhasldifjasdlkfj/c", "/a/b/1/c"})
    void matchesWithValidPath(String path) {
        Patterns patterns = new Patterns("/a/b/*/c");

        assertThat(patterns.matches(path)).isTrue();
    }

    @DisplayName("matches 메서드는 /** 경로를 가지고 있을 경우 하위 경로는 모두 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/a/b/c/d/e", "/a/b/ccalsdkfj", "/a/b/clkj/alsdkfj/alsdkfjl"})
    void matchWithWildCardPattern(String path) {
        Patterns patterns = new Patterns("/a/b/**");

        assertThat(patterns.matches(path)).isTrue();
    }

}
