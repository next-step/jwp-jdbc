package core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import study.jackson.Car;
import study.jackson.Color;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {

    static Stream toCarObjectProvider() {
        return Stream.of(
                Arguments.of("{\"color\":\"Black\",\"type\":\"BMW\"}", new Car(Color.Black, "BMW")),
                Arguments.of("{ \"color\": \"Black\", \"type\": \"BMW\" }", new Car(Color.Black, "BMW")),
                Arguments.of("{\"color\":\"Red\",\"type\":\"Bentz\"}", new Car(Color.Red, "Bentz"))
        );
    }

    static Stream toJsonProvider() {
        return Stream.of(
                Arguments.of(new Car(Color.Black, "BMW"), "{\"color\":\"Black\",\"type\":\"BMW\"}"),
                Arguments.of(new Car(Color.Red, "Bentz"), "{\"color\":\"Red\",\"type\":\"Bentz\"}")
        );
    }

    @DisplayName("Json 문자열을 객체로 변환")
    @ParameterizedTest(name = "jsonString: {0}, expected: {1}")
    @MethodSource("toCarObjectProvider")
    void toObject(final String jsonString, final Car expected) throws Exception {
        final Car car = JsonUtils.toObject(jsonString, Car.class);
        assertThat(car)
                .hasFieldOrPropertyWithValue("color", expected.getColor())
                .hasFieldOrPropertyWithValue("type", expected.getType());
    }

    @DisplayName("객체를 Json 문자열로 변환")
    @ParameterizedTest(name = "object: {0}, expected: {1}")
    @MethodSource("toJsonProvider")
    void toJsonString(final Object object, final String expected) throws JsonProcessingException {
        final String jsonString = JsonUtils.toString(object);
        assertThat(jsonString).isEqualTo(expected);
    }
}
