package core.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import study.jackson.Car;
import study.jackson.JacksonTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void toJsonString() {
        String expected = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = new Car("Black", "BMW");
        String actual = JsonUtils.toJsonString(car);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toJavaListFromJsonString() {
        List<Car> expected = sampleCars();
        String jsonCarArray =
                "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> actual = (List<Car>) JsonUtils.toObject(jsonCarArray, new TypeReference<List<Car>>(){});
    }

    public static List<Car> sampleCars() {
        return (List<Car>) new ArrayList() {{
            add(new Car("Black", "BMW"));
            add(new Car("Red", "FIAT"));
        }};
    }
}
