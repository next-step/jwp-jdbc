package core.mvc;

import org.junit.jupiter.api.Test;
import study.jackson.Car;
import study.jackson.Color;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo(Color.Black);
        assertThat(car.getType()).isEqualTo("BMW");
    }
}
