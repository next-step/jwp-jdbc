package core.mvc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class HttpServletUtilsTest {
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        om = new ObjectMapper();
    }


    @DisplayName("extract JsonBody and convert to Object")
    @Test
    void toObject() {
        //given
        User expected = new User("user", "123", "js", "test@mail.com");
        String jsonUserInfo = JsonUtils.toJson(om, expected);
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/api/users");
        mockRequest.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        mockRequest.setContent(jsonUserInfo.getBytes());
        HttpServletRequest request = mockRequest;

        //when
        User actual = (User) HttpServletUtils.jsonBodyToObject(om, request, User.class);

        //then
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }
}