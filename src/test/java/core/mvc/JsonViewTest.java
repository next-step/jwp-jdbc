package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import study.jackson.Car;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonViewTest {
    private static final Logger logger = LoggerFactory.getLogger( JsonViewTest.class );
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private View view;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        view = new JsonView();
        objectMapper = new ObjectMapper();
    }

    @Test
    void render_no_element() throws Exception {
        HashMap<String, Object> model = new HashMap<>();
        view.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(model));
    }

    @Test
    void render_one_element() throws Exception {
        Map<String, Object> model = new HashMap<>();
        Car expected = new Car("Black", "Sonata");
        model.put("car", expected);

        view.render(model, request, response);

        Car actual = JsonUtils.toObject(response.getContentAsString(), Car.class);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void render_over_two_element() throws Exception {
        Map<String, Object> model = new HashMap<>();
        Car expected = new Car("Black", "Sonata");
        model.put("car", expected);
        model.put("name", "pobi");

        view.render(model, request, response);

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        Map map = JsonUtils.toObject(response.getContentAsString(), HashMap.class);
        Map car = (Map) map.get("car");
        assertThat(car.get("color")).isEqualTo("Black");
        assertThat(car.get("type")).isEqualTo("Sonata");
        assertThat(map.get("name")).isEqualTo("pobi");
        logger.debug("response body : {}", response.getContentAsString());
    }
}
