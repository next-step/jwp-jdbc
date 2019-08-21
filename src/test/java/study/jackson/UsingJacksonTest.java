package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UsingJacksonTest {

    static Stream objectToJsonProvider() {
        return Stream.of(
                Arguments.of(
                        Color.Black,
                        "Sedan",
                        "{\"color\":\"Black\",\"type\":\"Sedan\"}"),
                Arguments.of(
                        Color.Red,
                        "SUV",
                        "{\"color\":\"Red\",\"type\":\"SUV\"}"),
                Arguments.of(
                        Color.White,
                        "Truck",
                        "{\"color\":\"White\",\"type\":\"Truck\"}")
        );
    }

    @DisplayName("Java Object에서 JSON 문자열로 변환")
    @ParameterizedTest(name = "Color: {0}, Type: {1}, Json: {2}")
    @MethodSource("objectToJsonProvider")
    void objectToJsonString(Color color, String type, String json) throws JsonProcessingException {
        Car car = new Car(color, type);
        ObjectMapper mapper = new ObjectMapper();
        final String jsonString = mapper.writeValueAsString(car);
        assertThat(jsonString).isEqualTo(json);
    }

    @DisplayName("JSON 문자열에서 Java Object 문자열로 변환")
    @ParameterizedTest(name = "Color: {0}, Type: {1}, Json: {2}")
    @MethodSource("objectToJsonProvider")
    void jsonStringToObject(Color color, String type, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final Car car = mapper.readValue(json, Car.class);
        assertThat(car)
                .hasFieldOrPropertyWithValue("color", color)
                .hasFieldOrPropertyWithValue("type", type);
    }
}
