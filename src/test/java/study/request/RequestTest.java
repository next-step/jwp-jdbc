package study.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Created by iltaek on 2020/07/02 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class RequestTest {

    @DisplayName("Request의 Body 읽는 테스트")
    @Test
    void readPOSTRequestBodyTest() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.POST.name());
        String expected = "Hello, World!";
        request.setContent(expected.getBytes());
        request.setContentType("text/plain");

        String result = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        assertThat(result).isEqualTo(expected);
    }
}
