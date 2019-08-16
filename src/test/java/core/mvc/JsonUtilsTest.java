package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import core.di.factory.BeanFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.jackson.Car;

import java.util.HashMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    @Test
    void toObject() throws Exception {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Car car = JsonUtils.toObject(json, Car.class);
        assertThat(car.getColor()).isEqualTo("Black");
        assertThat(car.getType()).isEqualTo("BMW");
    }

    @Test
    void mapToJson() throws Exception{
        HashMap<String, Car> map = new HashMap<>();
        map.put("1", new Car("RED", "SOSO"));
        map.put("2", new Car("BLUE", "SOSO@@@@@"));

        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(map);
        System.out.println(str);

        String str2 = objectMapper.writeValueAsString(map.values().stream()
                .map(car -> {
                    return getObjectToJson(objectMapper, car);
                }).collect(Collectors.toList()));

        System.out.println(str2);
    }

    public Object getObjectToJson(ObjectMapper objectMapper, Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("getObjectToJson Error {}", e.getMessage());
        }
        return "";
    }
}
