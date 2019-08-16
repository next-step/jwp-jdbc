package core.mvc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {

    static String testJson;
    @BeforeAll
    static void init() {

        testJson = "{\"color\":\"Black\",\"type\":\"BMW\"}";
    }

    @Test
    void readValueFromString() throws Exception {
        Car car = JsonUtils.readValue(testJson, Car.class);

        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void readValueFromReader() throws Exception {
        Reader reader = new StringReader(testJson);
        Car car = JsonUtils.readValue(reader, Car.class);

        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void readValueFromInput() throws Exception {
        InputStream input = new ByteArrayInputStream(testJson.getBytes());
        Car car = JsonUtils.readValue(input, Car.class);

        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void writeValue() throws Exception {
        Car car = new Car("Black", "BMW");
        String actual = JsonUtils.writeValue(car);

        assertThat(actual).isEqualTo(testJson);
    }
}
