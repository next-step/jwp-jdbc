package core.mvc.messageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import study.jackson.Car;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMessageConverterTest {

    private JsonMessageConverter converter;

    @BeforeEach
    public void setup() {
        converter = new JsonMessageConverter(JsonObjectMapper.jsonObjectMapperBuilder().build());
    }

    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = converter.readMessage(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("Object 1개인 경우")
    @Test
    void to_json_one_object() throws IOException {
        Car car = new Car("Black", "Sonata");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(car);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        converter.writeMessage(outputStream, car);

        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @DisplayName("Object 여러개인 경우")
    @Test
    void to_json_multi_object() throws IOException {
        List<Car> cars = Arrays.asList(
                new Car("Black", "Sonata"),
                new Car("White", "Sonata"),
                new Car("Blue", "Sonata")
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cars);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        converter.writeMessage(outputStream, cars);

        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @DisplayName("String value 인 경우")
    @Test
    void to_json_string() throws IOException {
        String car = "car";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(car);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        converter.writeMessage(outputStream, car);

        assertThat(response.getContentAsString()).isEqualTo(json);
    }
}