package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JacksonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void objectToJsonAsString() throws JsonProcessingException {
        Car car = new Car("yellow", "bmw");

        String writeValueAsString = objectMapper.writeValueAsString(car);

        assertEquals("{\"color\":\"yellow\",\"type\":\"bmw\"}", writeValueAsString);
    }

    @Test
    void jsonStringToObject() throws JsonProcessingException {
        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

        Car car = objectMapper.readValue(json, Car.class);

        assertEquals(new Car("Black", "BMW"), car);
    }
}
