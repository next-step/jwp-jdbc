package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

class JsonUtilsTest {

    @Test
    void toObject() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("Java Object를 Json 문자열로 변환한다")
    @Test
    void to_json_string() {
        final Car car = new Car("white", "e class avantgarde");
        String expected = "{\"color\":\"white\",\"type\":\"e class avantgarde\"}";
        String actual = JsonUtils.toJson(car);

        assertThat(actual).isEqualTo(expected);
    }
}
