package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultInterceptorMetaDataTest {

    @DisplayName("addInterceptor를 통해 인터셉터를 추가할 수 있다.")
    @Test
    void addInterceptor() {
        DefaultInterceptorMetaData data = new DefaultInterceptorMetaData();
        data.addInterceptor(new Interceptor() {});

        Interceptors interceptors = (Interceptors) ReflectionTestUtils.getField(data, "interceptors");

        assertThat(interceptors.stream().count()).isOne();
    }

    @DisplayName("addPathPatterns로 경로를 가변 적으로 추가할 수 있다.")
    @Test
    void addPathPattern() {
        DefaultInterceptorMetaData data = new DefaultInterceptorMetaData();

        data.addPathPatterns("/a/b/c");

        assertThat(data.matches("/a/b/c")).isTrue();
    }

    @DisplayName("excludePathPatterns로 제외할 경로를 추가할 수 있다")
    @Test
    void excludePathPatterns() {
        DefaultInterceptorMetaData data = new DefaultInterceptorMetaData();

        data.excludePathPatterns("/a/b/c");

        assertThat(data.matches("/a/b/c")).isFalse();
    }

    @DisplayName("matches 는 허용 경로에는 포함되고 예외 경로에는 포함되지 않는 경우에만 참을 반환한다.")
    @Test
    void matchWithPassURI() {
        DefaultInterceptorMetaData data = new DefaultInterceptorMetaData();

        data.excludePathPatterns("/a/b/c", "/a/b/c/d");
        data.addPathPatterns("/a/b/d", "/a/b/c/d");

        assertThat(data.matches("/a/b/c")).isFalse();
        assertThat(data.matches("/a/b/d")).isTrue();
        assertThat(data.matches("/a/b/c/d")).isFalse();
    }
}
