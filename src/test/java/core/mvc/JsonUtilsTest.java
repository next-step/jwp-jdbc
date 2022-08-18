package core.mvc;

import org.junit.jupiter.api.Test;
import study.jackson.Car;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilsTest {
    @Test
    void toObject() throws Exception {
        String json = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void fromObject() throws Exception {
        Car car = new Car("Black", "BMW");
        String json = JsonUtils.fromObject(car);

        assertThat(json).isEqualTo("{\"color\":\"Black\",\"type\":\"BMW\"}");
    }
}
