package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.annotation.web.RequestParam;
import core.mvc.tobe.MethodParameter;
import core.util.StringUtils;
import next.dto.UserUpdatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyArgumentResolverTest {

    private RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver();

    @DisplayName("RequestBody Argument Resolver Test")
    @ParameterizedTest(name = "MethodParameter: {0}")
    @MethodSource("sampleRequestBodyMethodParameter")
    void RequestBodyResolveArguments(HttpServletRequest request, MethodParameter mp, Object expectedResult) {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockUser result = (MockUser)argumentResolver.resolveArgument(mp, request, response);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> sampleRequestBodyMethodParameter() throws NoSuchMethodException, UnsupportedEncodingException {
        final Method mockRequestBodyMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyMethod", MockUser.class);
        final RequestBody userAnnotation = (RequestBody) mockRequestBodyMethod.getParameterAnnotations()[0][0];

        String paramName = "user";

        String expectedId = "ninjasul";
        String expectedName = "Park";
        int expectedAge = 27;
        long expectedMoney = 50000000L;

        MockUser expectedUser = new MockUser(expectedId);
        expectedUser.setName(expectedName);
        expectedUser.setAge(expectedAge);
        expectedUser.setMoney(expectedMoney);

        MockHttpServletRequest request = new MockHttpServletRequest();
        byte[] bytes = StringUtils.toJson(expectedUser).getBytes("UTF-8");
        request.setContent(bytes);

        return Stream.of(
                Arguments.of(request,
                        new MethodParameter(mockRequestBodyMethod, MockUser.class, new Annotation[]{userAnnotation}, paramName),
                             expectedUser)
        );
    }

}
