package core.mvc.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    private ObjectMapper om;

    @BeforeEach
    void setUp() {
        om = new ObjectMapper();
        om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    @DisplayName("json to object with objectMapper")
    @Test
    void toObject() throws Exception {
        //givne
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

        //when
        Car car = JsonUtils.toObject(om, json, Car.class);

        //then
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("object to json with objectMapper")
    @Test
    void toJson() throws Exception {
        String expected = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = new Car("Black", "BMW");
        String json = JsonUtils.toJson(om, car);

        assertThat(json).isEqualTo(expected);
    }
}
