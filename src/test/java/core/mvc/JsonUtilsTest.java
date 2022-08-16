package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonUtilsTest {
    @DisplayName("json 문자열을 객체로 변환")
    @Test
    void toObject() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("유효하지 않은 json 문자열을 객체로 변환중 예외 발생")
    @Test
    void toObject_illegal_json_format() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" ";

        assertThatThrownBy(() -> JsonUtils.toObject(json, Car.class))
                .isInstanceOf(ObjectMapperException.class);
    }

    @DisplayName("객체를 json 문자열로 변환")
    @Test
    void parseObjectToString() {
        Car car = new Car("Black", "BMW");
        String actual = JsonUtils.toString(car);
        assertThat(actual).isEqualTo("{\"color\":\"Black\",\"type\":\"BMW\"}");
    }
}
