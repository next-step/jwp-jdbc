package study.jackson;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by iltaek on 2020/07/02 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class JacksonTest {

    Logger logger = LoggerFactory.getLogger(JacksonTest.class);

    @DisplayName("Object -> JSON 테스트")
    @Test
    void Object2JSONTest() throws Exception {
        String expected = "{\"color\":\"black\",\"type\":\"sedan\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        Car car = new Car("black", "sedan");
        String carJson = objectMapper.writeValueAsString(car);
        logger.debug("Car JSON : {}", carJson);

        assertThat(carJson).isEqualTo(expected);
    }

    @DisplayName("JSON -> Object 테스트")
    @Test
    void JSON2ObjectTest() throws Exception {
        Car expected = new Car("black", "sedan");

        ObjectMapper objectMapper = new ObjectMapper();
        String carJson = "{\"color\":\"black\",\"type\":\"sedan\"}";
        Car actual = objectMapper.readValue(carJson, Car.class);
        logger.debug("Car Object : {}", actual);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("JSON -> JSON Node 테스트")
    @Test
    void JSON2JSONNode() throws Exception {
        String json = "{\"color\":\"black\",\"type\":\"sedan\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        assertThat(jsonNode.get("color").asText()).isEqualTo("black");
        assertThat(jsonNode.get("type").asText()).isEqualTo("sedan");
    }

    @DisplayName("JSON -> Map 테스트")
    @Test
    void JSON2Map() throws Exception {
        String json = "{\"color\":\"black\",\"type\":\"sedan\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

        assertThat(map.size()).isEqualTo(2);
        assertThat(map.get("color")).isEqualTo("black");
        assertThat(map.get("type")).isEqualTo("sedan");
    }

    @DisplayName("Map -> JSON 테스트")
    @Test
    void Map2JSON() throws Exception {
        String expected = "{\"color\":\"black\",\"type\":\"sedan\"}";
        Map<String, Object> map = Maps.newHashMap();
        map.put("color", "black");
        map.put("type", "sedan");

        ObjectMapper objectMapper = new ObjectMapper();

        logger.debug("{}", objectMapper.writeValueAsString(map));
        assertThat(objectMapper.writeValueAsString(map)).isEqualTo(expected);
    }
}
