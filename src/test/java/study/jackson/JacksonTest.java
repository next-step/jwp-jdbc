package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JacksonTest {

    private ObjectMapper objectMapper;
    public static final Car CAR = new Car("yellow", "renault");
    public static final String CAR_JSON_STRING = "{\"color\":\"yellow\",\"type\":\"renault\"}";

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @DisplayName("Java object를 json으로 변경")
    @Test
    public void javaObject2JSON() throws JsonProcessingException {
        assertThat(objectMapper.writeValueAsString(CAR)).isEqualTo(CAR_JSON_STRING);
    }

    @DisplayName("json을 Java Object로 변경")
    @Test
    public void JSON2JavaObject() throws IOException {
        assertThat(objectMapper.readValue(CAR_JSON_STRING, Car.class)).isEqualTo(CAR);
    }

    @DisplayName("json 배열 문자를 Java List로 변경")
    @Test
    public void createJavaListFromJsonArray() throws IOException {
        final List<Car> expected = sampleCars();
        String jsonCarArray =
                "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> actual = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>(){});
        assertThat(actual).isEqualTo(expected);
    }

    private List<Car> sampleCars() {
        return (List<Car>) new ArrayList() {{
                add(new Car("Black", "BMW"));
                add(new Car("Red", "FIAT"));
            }};
    }

    @DisplayName("json 배열 문자를 Java Map로 변경")
    @Test
    public void createJavaMapFromJSONString() throws IOException {
        final Map<String, String> expected = sampleCarMap();
        Map<String, String> actual = objectMapper.readValue(CAR_JSON_STRING, new TypeReference<Map<String, String>>(){});
        assertThat(actual).isEqualTo(expected);
    }

    private Map<String, String> sampleCarMap() {
        return (Map<String, String>) new HashMap() {{
                put("color", "yellow");
                put("type", "renault");
            }};
    }

    @DisplayName("configure 메소드를 통해 새 필드를 무시하도록 허용 되는지 여부")
    @Test
    public void configure_unknown_properties() throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonString = "{ \"color\" : \"Black\", \"type\" : \"Fiat\", \"year\" : \"1970\" }";

        Car car = objectMapper.readValue(jsonString, Car.class);

        JsonNode jsonRootNode = objectMapper.readTree(jsonString);
        JsonNode jsonYearNode = jsonRootNode.get("year");

        assertThat(jsonYearNode.asText()).isEqualTo("1970");
    }

    @DisplayName("사용자 정의 Serializer 생성")
    @Test
    public void custom_serializer() throws JsonProcessingException {
        registerSerializer();
        String expected = "{\"car_brand\":\"renault\"}";
        assertThat(objectMapper.writeValueAsString(CAR)).isEqualTo(expected);
    }

    private void registerSerializer() {
        SimpleModule module = new SimpleModule("CustomSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Car.class, new CustomCarSerializer());
        objectMapper.registerModule(module);
    }

    @DisplayName("사용자 정의 Deserializer 생성")
    @Test
    public void custom_deserializer() throws IOException {
        registerDeserializer();
        Car car = objectMapper.readValue(CAR_JSON_STRING, Car.class);
        assertThat(car.getColor()).isEqualTo("yellow");
        assertThat(car.getType()).isNull();
    }

    private void registerDeserializer() {
        SimpleModule module = new SimpleModule("CustomDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Car.class, new CustomCarDeserializer());
        objectMapper.registerModule(module);
    }

}
