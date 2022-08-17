package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.mvc.tobe.MethodParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestBodyQueryStringArgumentResolverTest {

    final RequestBodyQueryStringArgumentResolver resolver = new RequestBodyQueryStringArgumentResolver();

    @DisplayName("RequestBody 애노테이션이 없으면 변환을 할 수 없다")
    @Test
    void not_supports_without_request_body_annotation() throws NoSuchMethodException {

        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockUser", MockUser.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, MockUser.class, parameterAnnotations[0], "user");

        final boolean actual = resolver.supports(methodParameter);

        assertThat(actual).isFalse();
    }

    @DisplayName("RequestBody 애노테이션이 있으면 변환 할 수 있다")
    @Test
    void supports_with_request_body_annotation() throws NoSuchMethodException {

        final Method method = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyUser", MockUser.class);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final MethodParameter methodParameter = new MethodParameter(method, MockUser.class, parameterAnnotations[0], "user");

        final boolean actual = resolver.supports(methodParameter);

        assertThat(actual).isFalse();
    }
}
