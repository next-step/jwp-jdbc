package core.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.jackson.Car;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtilsTest.class);

    @Test
    void toObject() {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("model ")
    @Test
    void toJsonString_map_one_data() {
        Map<String, Object> model = new HashMap<>();
        model.put("color", "Black");

        String result = JsonUtils.toJsonString(model);

        String json = "{\"color\":\"Black\"}";
        assertThat(result).isEqualTo(json);
    }

    @DisplayName("map 객체를 jsonString 으로 파싱한다")
    @Test
    void toJsonString_map_two_data() {
        String expected = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Map<String, Object> model = new HashMap<>();
        model.put("color", "Black");
        model.put("type", "BMW");

        String result = JsonUtils.toJsonString(model);
        logger.debug("Map json parsing result : {}", result);

        assertThat(result).isEqualTo(expected);
    }
}