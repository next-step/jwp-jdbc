package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class InterceptorConfigTest {

    @DisplayName("인터셉터 추가")
    @Test
    void addInterceptor() {
        InterceptorConfig config = new InterceptorConfig();
        config.addInterceptor(new TimeLoggingInterceptor());

         List<Interceptor> interceptors = (List<Interceptor>) ReflectionTestUtils.getField(config, "interceptors");

        assertThat(interceptors.size()).isOne();
    }

    @DisplayName("pathPattern 추가")
    @Test
    void addPathPattern() {
        InterceptorConfig config = new InterceptorConfig();

        config.addPathPattern("/a/b/c");

        assertThat(config.match("/a/b/c")).isTrue();
    }

    @DisplayName("pathPattern 제외")
    @Test
    void excludePathPatterns() {
        InterceptorConfig config = new InterceptorConfig();

        config.excludePathPattern("/a/b/c");

        assertThat(config.match("/a/b/c")).isFalse();
    }
}