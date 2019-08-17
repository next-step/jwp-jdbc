package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import study.jackson.Car;

import javax.servlet.ServletOutputStream;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void toJson() throws IOException {
        Car car = new Car("Black", "Sonata");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(car);

        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        JsonUtils.toJson(outputStream, car);

        assertThat(response.getContentAsString()).isEqualTo(json);
    }
}
