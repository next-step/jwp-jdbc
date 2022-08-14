package core.mvc.tobe.support.converter;

import next.dto.UserCreatedDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class Jackson2HttpMessageConverterTest {

    @Test
    @DisplayName("싱글톤 객체")
    void instance() {
        assertThatNoException()
                .isThrownBy(Jackson2HttpMessageConverter::instance);
    }

    @Test
    @DisplayName("신규 생성 불가")
    void newInstance_thrownAssertionError() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(() -> ReflectionUtils.newInstance(Jackson2HttpMessageConverter.class));
    }

    @Test
    @DisplayName("타입을 미디어 타입에 따라 읽을 수 있는지 여부")
    void canRead() {
        assertAll(
                () -> assertThat(Jackson2HttpMessageConverter.instance().canRead(UserCreatedDto.class, MediaType.APPLICATION_JSON)).isTrue(),
                () -> assertThat(Jackson2HttpMessageConverter.instance().canRead(UserCreatedDto.class, MediaType.APPLICATION_XML)).isFalse()
        );
    }

    @Test
    @DisplayName("json 문자열을 객체로 변환")
    void read() {
        String id = "id";
        String password = "password";
        String name = "name";
        String email = "email@email.com";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        request.setContent(String.format("{\"userId\":\"%s\",\"password\":\"%s\", \"name\":\"%s\", \"email\":\"%s\"}", id, password, name, email).getBytes());
        //when
        Object result = Jackson2HttpMessageConverter.instance().read(UserCreatedDto.class, new ServletServerHttpRequest(request));
        //then
        assertThat(result).isEqualTo(new UserCreatedDto(id, password, name, email));
    }
}
