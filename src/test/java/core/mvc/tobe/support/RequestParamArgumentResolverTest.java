package core.mvc.tobe.support;

import core.annotation.web.RequestParam;
import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParamArgumentResolverTest {

    private RequestParamArgumentResolver argumentResolver = new RequestParamArgumentResolver();

    @DisplayName("RequestParam Argument Resolver Test")
    @ParameterizedTest(name = "MethodParameter: {0}")
    @MethodSource("sampleRequestParamMethodParameter")
    void RequestParamResolveArguments(HttpServletRequest request, MethodParameter mp, Object expectedResult) {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object result = argumentResolver.resolveArgument(mp, request, response);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> sampleRequestParamMethodParameter() throws NoSuchMethodException {
        final Method mockStringRequestParamMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestParamMethod", String.class);
        final RequestParam stringAnnotation = (RequestParam) mockStringRequestParamMethod.getParameterAnnotations()[0][0];

        final Method mockIntRequestParamMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestParamMethod", int.class);
        final RequestParam intAnnotation = (RequestParam) mockIntRequestParamMethod.getParameterAnnotations()[0][0];
        String paramName = "id";

        String expectedStringResult = "jun";
        int expectedIntResult = 50;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("user", expectedStringResult);
        request.setParameter("id", String.valueOf(expectedIntResult));

        return Stream.of(
                Arguments.of(request,
                        new MethodParameter(mockStringRequestParamMethod, String.class, new Annotation[]{stringAnnotation}, paramName),
                        expectedStringResult),
                Arguments.of(request,
                        new MethodParameter(mockIntRequestParamMethod, Integer.class, new Annotation[]{intAnnotation}, paramName),
                        expectedIntResult)
        );
    }

}
