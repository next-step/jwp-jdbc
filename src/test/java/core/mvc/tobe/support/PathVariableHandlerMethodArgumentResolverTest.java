package core.mvc.tobe.support;

import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PathVariableHandlerMethodArgumentResolverTest {

    @Test
    public void resolvePathVariableTest() throws NoSuchMethodException {
        HandlerMethodArgumentResolver resolver = new PathVariableHandlerMethodArgumentResolver();
        Method method = TestUserController.class.getDeclaredMethod("show_pathvariable", long.class);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/1");

        MethodParameter methodParameter = new MethodParameter(method, "id", 0);

        assertThat(resolver.supportsParameter(methodParameter)).isTrue();
        assertThat(resolver.resolveArgument(methodParameter, request)).isEqualTo(1L);
    }


}
