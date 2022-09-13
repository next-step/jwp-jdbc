package core.mvc;

import org.junit.jupiter.api.Test;
import study.jackson.Car;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @Test
    void toObject() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void objectToJson() {
        Car car = new Car("Black", "BMW");
        String json = JsonUtils.objectToJson(car);

        assertThat(json).isEqualTo("{\"color\":\"Black\",\"type\":\"BMW\"}");
    }
}
