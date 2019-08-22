package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import study.jackson.Car;
import study.jackson.Color;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonViewTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonViewTest.class);
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private View view;

    @BeforeEach
    void setUp() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.view = new JsonView();
    }

    @Test
    void render_no_element() throws Exception {
        this.view.render(new HashMap<>(), this.request, this.response);
        assertThat(this.response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(this.response.getContentAsString()).isBlank();
    }

    @Test
    void render_one_element() throws Exception {
        final Map<String, Object> model = new HashMap<>();
        final Car expected = new Car(Color.Black, "Sonata");
        model.put("car", expected);

        this.view.render(model, this.request, this.response);

        final Car actual = JsonUtils.toObject(this.response.getContentAsString(), Car.class);
        assertThat(this.response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void render_over_two_element() throws Exception {
        final Map<String, Object> model = new HashMap<>();
        final Car expected = new Car(Color.Black, "Sonata");
        model.put("car", expected);
        model.put("name", "포비");

        this.view.render(model, this.request, this.response);

        final String contentAsString = this.response.getContentAsString();
        assertThat(this.response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(contentAsString).isEqualTo("{\"car\":{\"color\":\"Black\",\"type\":\"Sonata\"},\"name\":\"포비\"}");
    }
}
