package core.mvc.tobe.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class RequestParameterUtilsTest {

    @DisplayName("queryString 으로 전달된 파라미터를 반환한다")
    @Test
    void queryStringParameter() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("hi", "hello");

        final RequestParameterUtils requestParameterUtils = new RequestParameterUtils(request);

        String actual = requestParameterUtils.getParameter("hi");

        assertThat(actual).isEqualTo("hello");
    }

    @DisplayName("json format request body 로 전달된 파라미터를 반환한다")
    @Test
    void jsonFormatRequestBodyParameter() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("hi", "hello");
        String json = "{\"car\":{\"color\":\"Black\",\"type\":\"Sonata\"},\"name\":\"pobi\"}";
        request.setContent(json.getBytes());

        final RequestParameterUtils requestParameterUtils = new RequestParameterUtils(request);

        String actual = requestParameterUtils.getParameter("color");

        assertThat(actual).isEqualTo("Black");
    }

    @DisplayName("query string request body 로 전달된 파라미터를 반환한다")
    @Test
    void QueryStringRequestBodyParameter() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("hi", "hello");
        String queryString = "color=Black&type=Sonata";
        request.setContent(queryString.getBytes());

        final RequestParameterUtils requestParameterUtils = new RequestParameterUtils(request);

        String actual = requestParameterUtils.getParameter("color");

        assertThat(actual).isEqualTo("Black");
    }

    @DisplayName("query string과 request body 모두 있는 경우 query string 파라미터가 우선순위가 높다")
    @Test
    void order_of_priority() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("color", "White");
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        request.setContent(json.getBytes());

        final RequestParameterUtils requestParameterUtils = new RequestParameterUtils(request);

        String actual = requestParameterUtils.getParameter("color");

        assertThat(actual).isEqualTo("White");
    }

    @DisplayName("존재 하지 않는 파라미터를 조회할 경우 null을 반환한다")
    @Test
    void null_parameter() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("hi", "hello");
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        request.setContent(json.getBytes());

        final RequestParameterUtils requestParameterUtils = new RequestParameterUtils(request);

        String actual = requestParameterUtils.getParameter("welcome");

        assertThat(actual).isNull();
    }
}
