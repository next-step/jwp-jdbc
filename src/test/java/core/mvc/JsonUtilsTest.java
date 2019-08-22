package core.mvc;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @DisplayName("toObject - 객체 Collection 변환할때")
    @Test
    void toObject1() throws Exception {
        String json = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Black1\", \"type\" : \"BMW1\" }]";
        List<Car> cars = JsonUtils.toObject(json, new TypeReference<List<Car>>() {});

        assertThat(cars.size()).isEqualTo(2);
        assertThat(cars.get(0).getColor()).isEqualTo("Black");
        assertThat(cars.get(0).getType()).isEqualTo("BMW");
        assertThat(cars.get(1).getColor()).isEqualTo("Black1");
        assertThat(cars.get(1).getType()).isEqualTo("BMW1");
    }

    @DisplayName("toObject - Reader 로 입력 받을때")
    @Test
    void toObject_Reader() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

        Car car = JsonUtils.toObject(new StringReader(json), Car.class);

        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void convertToJsonString() throws Exception {
        int id = 134;
        String name = "name!!!";
        Map<String, Object> model = new HashMap<>();
        model.put("id", id);
        model.put("name", name);

        String jsonString = JsonUtils.convertToJsonString(model);

        assertThat(jsonString).isEqualTo("{\"name\":\"name!!!\",\"id\":134}");
    }

    @DisplayName("convertToJsonString - 값이 객체 일때")
    @Test
    void convertToJsonString_Object() throws Exception {
        String color = "color!!";
        String type = "type!!";
        Car car = new Car(color, type);

        Map<String, Object> model = new HashMap<>();
        model.put("car", car);

        String jsonString = JsonUtils.convertToJsonString(model);

        assertThat(jsonString).isEqualTo("{\"car\":{\"color\":\"color!!\",\"type\":\"type!!\"}}");
    }

    @DisplayName("convertToJsonString - 값이 없을 때")
    @Test
    void convertToJsonString_EmptyModel() throws Exception {
        Map<String, ?> model = new HashMap<>();

        String jsonString = JsonUtils.convertToJsonString(model);

        assertThat(jsonString).isEqualTo("{}");
    }
}
