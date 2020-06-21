package core.mvc.tobe.support;

import core.annotation.web.RequestBody;
import core.mvc.JsonUtils;
import core.mvc.tobe.MethodParameter;
import next.dto.UserCreatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class RequestBodyArgumentResolverTest {

    private final RequestBodyArgumentResolver resolver = new RequestBodyArgumentResolver();

    @DisplayName("json 요청을 잘 파싱하는지 테스트한다.")
    @Test
    void test_parse_json_body() throws Exception {
        // given
        final Method method = RequestBodyArgumentResolverTest.class.getMethod("test", UserCreatedDto.class);
        final MethodParameter methodParameter =
                new MethodParameter(method, UserCreatedDto.class, method.getParameterAnnotations()[0], "dto");
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final UserCreatedDto expected =
                new UserCreatedDto("hyeyoom", "1234", "chwon", "neoul_chw@icloud.com");
        final String json = JsonUtils.stringify(expected);
        request.setContent(json.getBytes());

        // when
        final UserCreatedDto actual = (UserCreatedDto) resolver.resolveArgument(methodParameter, request, response);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    public void test(@RequestBody UserCreatedDto dto) {
        // no-op
    }
}