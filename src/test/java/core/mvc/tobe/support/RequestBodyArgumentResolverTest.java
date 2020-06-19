package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
import core.mvc.tobe.MethodParameter;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
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

class RequestBodyArgumentResolverTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private RequestBodyArgumentResolver argumentResolver = new RequestBodyArgumentResolver();

    @DisplayName("RequestBody Argument Resolver Test")
    @ParameterizedTest(name = "MethodParameter: {0}")
    @MethodSource("sampleRequestBodyMethodParameter")
    void PathVariableResolveArguments(HttpServletRequest request, MethodParameter mp, Object expectedResult) {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object result = argumentResolver.resolveArgument(mp, request, response);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> sampleRequestBodyMethodParameter() throws NoSuchMethodException, JsonProcessingException {
        final Method mockUpdateUserRequestBodyMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBody", UserUpdatedDto.class);
        final RequestBody updateUserAnnotation = (RequestBody) mockUpdateUserRequestBodyMethod.getParameterAnnotations()[0][0];
        final UserUpdatedDto userUpdatedDto = new UserUpdatedDto("name", "email@dot.com");
        final MockHttpServletRequest updateRequest = new MockHttpServletRequest("POST", "/requestBody");
        updateRequest.setContent(objectMapper.writeValueAsBytes(userUpdatedDto));

        final Method mockCreateUserRequestBodyMethod = MockArgumentResolverController.class.getDeclaredMethod("mockRequestBody", UserCreatedDto.class);
        final RequestBody createUserAnnotation = (RequestBody) mockCreateUserRequestBodyMethod.getParameterAnnotations()[0][0];
        final UserCreatedDto userCreatedDto = new UserCreatedDto("id", "pw", "name", "email@dot.com");
        final MockHttpServletRequest createRequest = new MockHttpServletRequest("POST", "/requestBody");
        createRequest.setContent(objectMapper.writeValueAsBytes(userCreatedDto));

        return Stream.of(
                Arguments.of(updateRequest,
                        new MethodParameter(mockUpdateUserRequestBodyMethod, UserUpdatedDto.class, new Annotation[]{updateUserAnnotation}, "userUpdateDto"),
                        userUpdatedDto),

                Arguments.of(createRequest,
                        new MethodParameter(mockCreateUserRequestBodyMethod, UserCreatedDto.class, new Annotation[]{createUserAnnotation}, "userCreateDto"),
                        userCreatedDto)
        );
    }
}