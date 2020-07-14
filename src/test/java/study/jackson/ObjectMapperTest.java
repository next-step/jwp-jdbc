package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperTest.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("car를 JSON으로! 다시 JSON을 car로!")
    @Test
    void test_car() throws JsonProcessingException {
        // given
        Car car = new Car("blue", "palisade");
        // when
        String json = objectMapper.writeValueAsString(car);
        // then
        assertThat(json).isEqualTo("{\"color\":\"blue\",\"type\":\"palisade\"}");

        Car newCar = objectMapper.readValue(json, Car.class);
        assertThat(newCar).isEqualTo(car);
    }

    @DisplayName("자바 Map을 JSON으로")
    @Test
    void test_map() throws JsonProcessingException {
        // given
        Car car = new Car("blue", "palisade");

        Map<String, Object> model = new HashMap<>();
        model.put("name", "crystal");
        model.put("age", 26);
        model.put("car", car);
        // when
        String json = objectMapper.writeValueAsString(model);
        // then
        assertThat(json).isEqualTo("{\"car\":{\"color\":\"blue\",\"type\":\"palisade\"},\"name\":\"crystal\",\"age\":26}");
    }
}
