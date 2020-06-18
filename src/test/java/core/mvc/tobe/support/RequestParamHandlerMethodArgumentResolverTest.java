package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.TestUser;
import core.mvc.tobe.TestUserController;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestParamHandlerMethodArgumentResolverTest {

    @Test
    public void resolvePrimitiveOrWrappingClassTest() throws NoSuchMethodException {
        HandlerMethodArgumentResolver resolver = new RequestParamHandlerMethodArgumentResolver(new ObjectMapper());

        Method method = TestUserController.class.getDeclaredMethod("create_int_long", long.class, int.class);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("id", "1");
        request.addParameter("age", "22");

        MethodParameter idMethodParameter = new MethodParameter(method, "id", 0);
        MethodParameter ageMethodParameter = new MethodParameter(method, "age", 1);

        assertThat(resolver.resolveArgument(idMethodParameter, request)).isEqualTo(1L);
        assertThat(resolver.resolveArgument(ageMethodParameter, request)).isEqualTo(22);
    }

    @Test
    public void resolveBeanTest() throws NoSuchMethodException {
        HandlerMethodArgumentResolver resolver = new RequestParamHandlerMethodArgumentResolver(new ObjectMapper());

        Method method = TestUserController.class.getDeclaredMethod("create_javabean", TestUser.class);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.addParameter("userId", "KingCjy");
        request.addParameter("password", "q1w2e3");
        request.addParameter("age", "22");

        TestUser testUser = new TestUser("KingCjy", "q1w2e3", 22);

        MethodParameter methodParameter = new MethodParameter(method, "testUser", 0);

        assertThat(resolver.resolveArgument(methodParameter, request)).isEqualTo(testUser);
    }
}
