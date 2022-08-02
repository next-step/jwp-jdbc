package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyArgumentResolverTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver();

    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
    }

    @DisplayName("RequestBody 를 UserCreatedDto 로 변환 가능해야한다.")
    @Test
    void resolveUserCreatedDto() throws NoSuchMethodException, JsonProcessingException {
        final Method mockRequestBodyMethod = MockArgumentResolverController.class
                .getDeclaredMethod(
                        "mockRequestBodyMethod",
                        UserCreatedDto.class
                );
        final RequestBody requestBodyAnnotation = (RequestBody) mockRequestBodyMethod.getParameterAnnotations()[0][0];

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/requestBody");
        request.setContent(objectMapper.writeValueAsBytes(
                new UserCreatedDto("userid", "password", "name", "email@gmail.com")
        ));
        MethodParameter methodParameter = new MethodParameter(
                mockRequestBodyMethod,
                UserCreatedDto.class,
                new Annotation[]{requestBodyAnnotation},
                "userCreatedDto"
        );

        Object actual = argumentResolver.resolveArgument(methodParameter, request, response);
        UserCreatedDto expected = new UserCreatedDto("userid", "password", "name", "email@gmail.com");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("RequestBody 를 UserUpdatedDto 로 변환 가능해야한다.")
    @Test
    void resolveUserUpdatedDto() throws NoSuchMethodException, JsonProcessingException {
        final Method mockRequestBodyMethod = MockArgumentResolverController.class
                .getDeclaredMethod(
                        "mockRequestBodyMethod",
                        UserUpdatedDto.class
                );
        final RequestBody requestBodyAnnotation = (RequestBody) mockRequestBodyMethod.getParameterAnnotations()[0][0];

        final MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/requestBody");
        request.setContent(objectMapper.writeValueAsBytes(
                new UserUpdatedDto("name", "email@gmail.com")
        ));
        MethodParameter methodParameter = new MethodParameter(
                mockRequestBodyMethod,
                UserUpdatedDto.class,
                new Annotation[]{requestBodyAnnotation},
                "userUpdatedDto"
        );

        Object actual = argumentResolver.resolveArgument(methodParameter, request, response);
        UserUpdatedDto expected = new UserUpdatedDto("name", "email@gmail.com");
        assertThat(actual).isEqualTo(expected);
    }
}
