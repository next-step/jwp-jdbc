package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Car 객체가 Json String 으로 변환되는지 확인한다.")
    void writeValueAsString() throws JsonProcessingException {
        Car car = new Car("yellow", "renault");
        String expected = "{\"color\":\"yellow\",\"type\":\"renault\"}";

        String jsonString = objectMapper.writeValueAsString(car);

        assertThat(jsonString).isEqualTo(expected);
    }

    @Test
    @DisplayName("Json String 가 Car객체로 변환 되는지 확인한다.")
    void readValue() throws IOException {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car expectedCar = new Car("Black", "BMW");

        Car actualCar = objectMapper.readValue(json, Car.class);

        assertThat(actualCar).isEqualTo(expectedCar);
    }

    @Test
    @DisplayName("Json String이 Car list 로 파싱 되는지 확인한다.")
    void typeReferenceOfList() throws IOException {
        String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> expectedList = getExpectedList();

        List<Car> actualList = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});

        assertThat(actualList).containsAll(expectedList);
    }

    private List<Car> getExpectedList() {
        return Lists.newArrayList(new Car("Black", "BMW"), new Car("Red", "FIAT"));
    }

    @Test
    @DisplayName("Json String이 Map으로 파싱 되는지 확인한다.")
    void typeReferenceOfMap() throws IOException {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Map<String, Object> expectedMap = getTestMap();
        Map<String, Object> actualMap
                = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});

        assertThat(actualMap).isEqualTo(expectedMap);
    }

    @Test
    @DisplayName("Map이 Json String으로 변하는지 확인한다.")
    void convertMapToJsonString() throws JsonProcessingException {
        String expected = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        String actual = objectMapper.writeValueAsString(getTestMap());

        assertThat(actual).isEqualTo(expected);
    }

    private Map<String, Object> getTestMap() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("color", "Black");
        expectedMap.put("type", "BMW");

        return expectedMap;
    }
}
