package core.mvc;

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
    void toJson() throws Exception {
        Car car = new Car("Black","BMW");
        String jsonStr = JsonUtils.toJson(car);
        String json = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        assertThat(jsonStr).isEqualTo(json);
    }
}
