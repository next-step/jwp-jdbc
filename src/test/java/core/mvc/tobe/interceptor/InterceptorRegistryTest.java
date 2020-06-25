package core.mvc.tobe.interceptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InterceptorRegistryTest {
    private TestInterceptor testInterceptor;
    private TestInterceptor testInterceptor2;
    private TestMappedInterceptor mappedInterceptor1;
    private TestMappedInterceptor mappedInterceptor2;
    private TestMappedInterceptor mappedInterceptor3;
    private TestMappedInterceptor mappedInterceptor4;

    private InterceptorRegistry registry;

    @BeforeEach
    void setUp() {
        testInterceptor = new TestInterceptor();
        testInterceptor2 = new TestInterceptor();
        mappedInterceptor1 = new TestMappedInterceptor("/users");
        mappedInterceptor2 = new TestMappedInterceptor("/users", "/users/form", "/users/loginForm");
        mappedInterceptor3 = new TestMappedInterceptor("/users", "/users/{id}", "/path/{id}");
        mappedInterceptor4 = new TestMappedInterceptor("/users", "/path/{id}/{name}");

        registry = new InterceptorRegistry()
            .addInterceptor(testInterceptor)
            .addInterceptor(testInterceptor2)
            .addInterceptor(mappedInterceptor1)
            .addInterceptor(mappedInterceptor2)
            .addInterceptor(mappedInterceptor3)
            .addInterceptor(mappedInterceptor4);
    }

    @Test
    @DisplayName("null Interceptor에 대한 테스트")
    void test_forNullInterceptorRegistry() {
        InterceptorRegistry registry = new InterceptorRegistry();

        assertThat(registry.getMatchedInterceptors("/users")).isEmpty();
    }

    @Test
    @DisplayName("url 맵핑 테스트")
    void test_urlMapping() {
        assertThat(registry.getMatchedInterceptors("/users")).contains(
            testInterceptor,
            testInterceptor2,
            mappedInterceptor1,
            mappedInterceptor2,
            mappedInterceptor3,
            mappedInterceptor4
        );
    }

    @Test
    @DisplayName("url 과 path 동시 맵핑 테스트")
    void test_urlAndPathMapping() {
        assertThat(registry.getMatchedInterceptors("/users/form")).contains(
            testInterceptor,
            testInterceptor2,
            mappedInterceptor2,
            mappedInterceptor3
        );
    }

    @Test
    @DisplayName("url 맵핑 Interceptor 테스트3")
    void test3() {
        assertThat(registry.getMatchedInterceptors("/users/loginForm")).contains(
            testInterceptor,
            testInterceptor2,
            mappedInterceptor2
        );
    }

    @Test
    @DisplayName("Path 맵핑 테스트")
    void test_pathMapping() {
        assertThat(registry.getMatchedInterceptors("/path/ninjasul")).contains(
            testInterceptor,
            testInterceptor2,
            mappedInterceptor3
        );
    }

    @Test
    @DisplayName("2-Depth Path 맵핑 테스트")
    void test_twoDepthPathMapping() {
        assertThat(registry.getMatchedInterceptors("/path/ninjasul/park")).contains(
            testInterceptor,
            testInterceptor2,
            mappedInterceptor4
        );
    }

    @Test
    @DisplayName("3-Depth Path 맵핑 테스트")
    void test_threeDepthPathMapping() {
        assertThat(registry.getMatchedInterceptors("/path/ninjasul/park/kim")).contains(
            testInterceptor,
            testInterceptor2
        );
    }
}