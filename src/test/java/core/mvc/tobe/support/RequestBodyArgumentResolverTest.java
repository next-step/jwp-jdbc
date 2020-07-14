package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyArgumentResolverTest {

    private RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver(Arrays.asList(new JsonMessageConverter()));

    @Test
    void requestBodyResolveArguments() throws NoSuchMethodException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockUserDto userDto = new MockUserDto("rim", "crystal", 26, 100000000000L);
        String jsonString = JsonUtils.toJsonString(userDto);

        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent(jsonString.getBytes());

        final Method requestBodyMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBodyMethod", MockUserDto.class);
        final RequestBody stringAnnotation = (RequestBody) requestBodyMethod.getParameterAnnotations()[0][0];

        MethodParameter mp = new MethodParameter(requestBodyMethod, MockUserDto.class, new Annotation[]{stringAnnotation}, "user");

        MockUserDto user = (MockUserDto) argumentResolver.resolveArgument(mp, request, response);

        assertThat(user.getId()).isEqualTo("rim");
        assertThat(user.getName()).isEqualTo("crystal");
        assertThat(user.getAge()).isEqualTo(26);
        assertThat(user.getMoney()).isEqualTo(100000000000L);
    }
}