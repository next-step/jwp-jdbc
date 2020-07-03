package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import java.lang.reflect.Method;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Created by iltaek on 2020/07/03 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
class RequestBodyArgumentResolverTest {

    private static final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private RequestBodyArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new RequestBodyArgumentResolver();
    }

    @DisplayName("isSupport 메서드 테스트")
    @Test
    void RequestBodySupportTest() throws NoSuchMethodException {
        final MethodParameter mp = getMethodParameter();
        assertThat(resolver.supports(mp)).isTrue();
    }

    @DisplayName("resolve 메서드 테스트")
    @Test
    void RequestBodyJsonToObjectTest() throws NoSuchMethodException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MethodParameter mp = getMethodParameter();
        final MockUserDto expected = new MockUserDto("1");

        request.setMethod(HttpMethod.POST.name());
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setRequestURI("/requestBody");
        request.setContent(JsonUtils.toJson(expected).getBytes());

        final MockUserDto actual = (MockUserDto) resolver.resolveArgument(mp, request, response);
        assertThat(actual).isEqualTo(expected);
    }

    private MethodParameter getMethodParameter() throws NoSuchMethodException {
        final Method modelMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyMethod", MockUserDto.class);
        final String[] parameterNames = nameDiscoverer.getParameterNames(modelMethod);
        return new MethodParameter(modelMethod, MockUserDto.class, modelMethod.getParameterAnnotations()[0],
            Objects.requireNonNull(parameterNames)[0]);
    }
}