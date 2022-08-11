package core.mvc.tobe.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.RequestBody;
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

    private final ArgumentResolver argumentResolver = new RequestBodyArgumentResolver();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("HTTP 요청 body 메시지로 핸들러 메서드의 인자를 생성한다.")
    @Test
    void resolveArgument() throws Exception {
        Class<MockArgumentResolverController> clazz = MockArgumentResolverController.class;
        Method method = clazz.getDeclaredMethod("createMockUser", MockUser.class);
        RequestBody requestBody = (RequestBody) method.getParameterAnnotations()[0][0];

        MockUser mockUser = new MockUser("test_user");
        mockUser.setName("홍길동");
        mockUser.setAddr("서울특별시");
        mockUser.setAge(30);
        mockUser.setMoney(10_000L);

        MethodParameter methodParameter = new MethodParameter(method, MockUser.class, new Annotation[]{requestBody}, "user");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(objectMapper.writeValueAsString(mockUser).getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockUser user = (MockUser) argumentResolver.resolveArgument(methodParameter, request, response);
        assertThat(user.getId()).isEqualTo("test_user");
        assertThat(user.getName()).isEqualTo("홍길동");
        assertThat(user.getAddr()).isEqualTo("서울특별시");
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getMoney()).isEqualTo(10_000L);
    }
}
