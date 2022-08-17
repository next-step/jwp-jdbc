package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyArgumentResolverTest {

    private RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver();

    @DisplayName("RequestBody Argument Resolver Test")
    @Test
    void RequestParamResolveArguments() throws NoSuchMethodException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockUser mockUser = new MockUser("jun");
        mockUser.setName("dong");
        mockUser.setAge(20);
        mockUser.setAddr("서울");
        mockUser.setMoney(10000L);

        request.setContent(JsonUtils.toString(mockUser).getBytes(StandardCharsets.UTF_8));

        final Method modelMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBody", MockUser.class);
        final RequestBody requestBodyAnnotation = (RequestBody) modelMethod.getParameterAnnotations()[0][0];

        MethodParameter mp = new MethodParameter(modelMethod, MockUser.class, new Annotation[]{requestBodyAnnotation}, "user");

        MockUser user = (MockUser) argumentResolver.resolveArgument(mp, request, response);

        assertThat(user.getId()).isEqualTo("jun");
        assertThat(user.getName()).isEqualTo("dong");
        assertThat(user.getAddr()).isEqualTo("서울");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getMoney()).isEqualTo(10000L);
    }

}
