package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.tobe.MethodParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestBodyQueryStringArgumentResolverTest {

    final RequestBodyQueryStringArgumentResolver resolver = new RequestBodyQueryStringArgumentResolver();

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
}
