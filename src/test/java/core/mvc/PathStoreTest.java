package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PathStoreTest {

    @DisplayName("pattern에 매칭되는 경로면 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"/a/b/azx/c", "/a/b/1111/c"})
    void matchingPath(String path) {
        PathPatternStore store = new PathPatternStore(Set.of("/a/b/*/c"));
        assertThat(store.match(path)).isTrue();
    }

    @DisplayName("와일드카드(*) 가 잘 동작하는지 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"/a/b/c/d/e", "/a/b/aszxcvq", "/a/b/1234aszcx/sadz/124a"})
    void matchWildCard(String path) {

        PathPatternStore store = new PathPatternStore(Set.of("/a/b/**"));
        assertThat(store.match(path)).isTrue();
    }
}