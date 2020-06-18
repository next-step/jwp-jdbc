package core.mvc.tobe.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.tobe.HandlerMethodArgumentResolver;
import core.mvc.tobe.MethodParameter;
import core.mvc.tobe.TestUserController;
import next.dto.UserCreatedDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class RequestBodyHandlerMethodArgumentResolverTest {

    private ObjectMapper objectMapper;
    private HandlerMethodArgumentResolver resolver;

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
        resolver = new RequestBodyHandlerMethodArgumentResolver(objectMapper);
    }

    @DisplayName("@RequestBody 파라미터 resolve Test")
    @Test
    public void resolveTest() throws JsonProcessingException, NoSuchMethodException {
        UserCreatedDto expected = new UserCreatedDto("pobi", "password", "포비", "pobi@nextstep.camp");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/users");
        request.setContent(objectMapper.writeValueAsString(expected).getBytes());

        Method method = TestUserController.class.getDeclaredMethod("create_user", UserCreatedDto.class);
        MethodParameter methodParameter = new MethodParameter(method, "userCreatedDto", 0);

        Object actual = resolver.resolveArgument(methodParameter, request);

        assertThat(actual).isEqualTo(expected);
    }
}
