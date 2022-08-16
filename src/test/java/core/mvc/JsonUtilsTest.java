package core.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

class JsonUtilsTest {

    @Test
    void toObject() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.parse(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("Java Object를 Json 문자열로 변환한다")
    @Test
    void to_json_string() {
        final Car car = new Car("white", "e class avantgarde");
        String expected = "{\"color\":\"white\",\"type\":\"e class avantgarde\"}";
        String actual = JsonUtils.stringify(car);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("json 문자열에서 특정 파라미터를 조회한다")
    @Test
    void find_value() {
        final Car car = new Car("white", "e class avantgarde");
        final String jsonString = JsonUtils.stringify(car);

        final String actual = JsonUtils.getAsStringOrNull(jsonString, "color");

        assertThat(actual).isEqualTo("white");
    }

    @DisplayName("json 문자열에서 존재하지 않는 속성를 조회하면 null을 반환한다")
    @Test
    void has_no_value() {
        final Car car = new Car("white", "e class avantgarde");
        final String jsonString = JsonUtils.stringify(car);

        final String actual = JsonUtils.getAsStringOrNull(jsonString, "name");

        assertThat(actual).isNull();
    }
}
