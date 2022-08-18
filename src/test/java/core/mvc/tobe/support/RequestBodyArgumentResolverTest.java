package core.mvc.tobe.support;

import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.support.converter.HttpMessageConverter;
import core.mvc.tobe.support.converter.Jackson2HttpMessageConverter;
import next.dto.UserCreatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("요청 바디 인자 가져오기")
class RequestBodyArgumentResolverTest {

    private static final List<HttpMessageConverter> JACKSON_CONVERTERS = List.of(Jackson2HttpMessageConverter.instance());

    @Test
    @DisplayName("변환기들로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> RequestBodyArgumentResolver.from(JACKSON_CONVERTERS));
    }

    @Test
    @DisplayName("리스트는 필수")
    void instance_null_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> RequestBodyArgumentResolver.from(null));
    }

    @Test
    @DisplayName("requestBody 애노테이션이 있으면 지원")
    void supports() throws NoSuchMethodException {
        RequestBodyArgumentResolver.from(JACKSON_CONVERTERS).supports(dtoParameterMethodWithRequestBodyMethodParameter());
    }

    @Test
    @DisplayName("jackson converter 로 인자 변환")
    void resolveArgument() throws NoSuchMethodException {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        String email = "email@email.com";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        request.setContent(String.format("{\"userId\":\"%s\",\"password\":\"%s\", \"name\":\"%s\", \"email\":\"%s\"}", id, password, name, email).getBytes());
        RequestBodyArgumentResolver jacksonMethodProcessor = RequestBodyArgumentResolver.from(JACKSON_CONVERTERS);
        //when
        Object result = jacksonMethodProcessor.resolveArgument(dtoParameterMethodWithRequestBodyMethodParameter(), request, new MockHttpServletResponse());
        //then
        assertThat(result).isEqualTo(new UserCreatedDto(id, password, name, email));
    }

    private MethodParameter dtoParameterMethodWithRequestBodyMethodParameter() throws NoSuchMethodException {
        Method method = RequestBodyArgumentResolverTest.class
                .getDeclaredMethod("dtoParameterMethodWithRequestBody", UserCreatedDto.class);
        Parameter parameter = method.getParameters()[0];
        return new MethodParameter(method, parameter.getType(), parameter.getAnnotations(), "dto");
    }

    private Object dtoParameterMethodWithRequestBody(@RequestBody UserCreatedDto dto) throws NoSuchMethodException {
        return null;
    }
}
