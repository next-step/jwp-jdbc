package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.MethodParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RequestBodyArgumentResolverTest {

    private final RequestBodyArgumentResolver resolver = new RequestBodyArgumentResolver();

    @DisplayName("파라미터에 RequestBody 애노테이션의 존재 여부에 따라 변환할 수 있다")
    @ParameterizedTest(name = "[{arguments}]")
    @MethodSource
    void supports_with_request_body_annotation(final String methodName, final boolean expected) throws NoSuchMethodException {
        final Method method = MockArgumentResolverController.class.getDeclaredMethod(methodName, MockUser.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, MockUser.class, parameterAnnotations[0], "user");

        final boolean actual = resolver.supports(methodParameter);

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> supports_with_request_body_annotation() {
        return Stream.of(
            Arguments.of("mockUser", false),
            Arguments.of("mockRequestBodyUser", true)
        );
    }

    @DisplayName("RequestBody의 값을 변환 - model")
    @ParameterizedTest(name = "[{arguments}]")
    @ValueSource(strings = {
        "id=admin&name=테스트유저&age=20&money=10000&addr=대한민국",
        "{\"id\":\"admin\",\"name\":\"테스트유저\",\"age\":\"20\",\"money\":\"10000\",\"addr\":\"대한민국\"}"
    })
    void request_body_by_format(String postData) throws NoSuchMethodException {
        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyUser", MockUser.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, MockUser.class, parameterAnnotations[0], "user");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(postData.getBytes());

        final MockUser actual = (MockUser) resolver.resolveArgument(methodParameter, request, response);

        assertThat(actual).isEqualTo(expectedMockUser());
    }

    @DisplayName("RequestBody의 값을 변환 - string")
    @Test
    void request_body_by_string() throws NoSuchMethodException {
        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyUserId", String.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, String.class, parameterAnnotations[0], "userId");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("userId=admin".getBytes());
        final String actual = (String) resolver.resolveArgument(methodParameter, request, response);

        assertThat(actual).isEqualTo("admin");
    }

    @DisplayName("RequestBody의 값을 변환 - int")
    @Test
    void request_body_by_int() throws NoSuchMethodException {
        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyAge", int.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, int.class, parameterAnnotations[0], "age");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("{\"age\":\"20\"}".getBytes());

        final Integer actual = (Integer) resolver.resolveArgument(methodParameter, request, response);

        assertThat(actual).isEqualTo(20);
    }

    @DisplayName("RequestBody의 값을 변환 - int and long ")
    @Test
    void request_body_by_int_and_long() throws NoSuchMethodException {
        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyAgeAndMoney", int.class, long.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter ageMethodParameter = new MethodParameter(method, int.class, parameterAnnotations[0], "age");
        final MethodParameter longMethodParameter = new MethodParameter(method, long.class, parameterAnnotations[1], "money");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent("{\"age\":\"20\",\"money\":\"10000\"}".getBytes());

        final Integer ageActual = (Integer) resolver.resolveArgument(ageMethodParameter, request, response);
        final Long longActual = (Long) resolver.resolveArgument(longMethodParameter, request, response);

        assertThat(ageActual).isEqualTo(20);
        assertThat(longActual).isEqualTo(10_000L);
    }

    private MockUser expectedMockUser() {
        final MockUser mockUser = new MockUser("admin");
        mockUser.setName("테스트유저");
        mockUser.setAge(20);
        mockUser.setMoney(10_000L);
        mockUser.setAddr("대한민국");
        return mockUser;
    }
}
