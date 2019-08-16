package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

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
    void toJsonString() throws JsonProcessingException {
        Car car = new Car("yellow", "renault");
        String expected = "{\"color\":\"yellow\",\"type\":\"renault\"}";
        String actual = JsonUtils.toJsonString(car);

        assertThat(actual).isEqualTo(expected);
    }
}
