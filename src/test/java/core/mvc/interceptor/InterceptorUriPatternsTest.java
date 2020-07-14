package core.mvc.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorUriPatternsTest {

    @DisplayName("Pattern 매칭하기")
    @Test
    void matches() {
        /* given */
        InterceptorUriPatterns interceptorUriPatterns = new InterceptorUriPatterns();
        interceptorUriPatterns.addAll(Arrays.asList("/api/users", "/login/**"));

        /* when */
        boolean result1 = interceptorUriPatterns.matches("/api/users");
        boolean result2 = interceptorUriPatterns.matches("/api");
        boolean result3 = interceptorUriPatterns.matches("/api/users/test");

        /* then */
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }

    @DisplayName("등록된 Pattern이 없으면 패턴을 통과한다.")
    @Test
    void matches2() {
        /* given */
        InterceptorUriPatterns interceptorUriPatterns = new InterceptorUriPatterns();

        /* when */
        boolean result = interceptorUriPatterns.matches("/api/users");

        /* then */
        assertThat(result).isTrue();
    }

}
