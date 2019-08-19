package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @DisplayName("json to object")
    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("object to json")
    @Test
    void toJson() throws Exception {
        String expected = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = new Car("Black", "BMW");
        String json = JsonUtils.toJson(car);

        assertThat(json).isEqualTo(expected);
    }
}
