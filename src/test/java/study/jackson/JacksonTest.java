package study.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Car car = new Car("pink", "lamborghini centenario-roadster");

    @Test
    void writeValueAsString() throws IOException {
        System.out.println(objectMapper.writeValueAsString(car));
    }

    @Test
    void writeValueAsBytes() throws IOException {
        final byte[] bytes = objectMapper.writeValueAsBytes(car);

        assertThat(new String(bytes)).isEqualTo(objectMapper.writeValueAsString(car));
    }

    @Test
    void readTreeByString() throws IOException {
        final JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(car));

        assertThat(node.get("color").asText()).isEqualTo(car.getColor());
        assertThat(node.get("type").asText()).isEqualTo(car.getType());
    }

    @Test
    void readTreeByBytes() throws IOException {
        final JsonNode node = objectMapper.readTree(objectMapper.writeValueAsBytes(car));

        assertThat(node.get("color").asText()).isEqualTo(car.getColor());
        assertThat(node.get("type").asText()).isEqualTo(car.getType());
    }

    @Test
    void readValueByString() throws IOException {
        final Car readCar = objectMapper.readValue(objectMapper.writeValueAsString(car), Car.class);

        assertThat(readCar.getColor()).isEqualTo(car.getColor());
        assertThat(readCar.getType()).isEqualTo(car.getType());
    }

    @Test
    void readValueByBytes() throws IOException {
        final Car readCar = objectMapper.readValue(objectMapper.writeValueAsBytes(car), Car.class);

        assertThat(readCar.getColor()).isEqualTo(car.getColor());
        assertThat(readCar.getType()).isEqualTo(car.getType());
    }

    @Test
    void readValueToMap() throws IOException{
        final Map<String, String> map = objectMapper.readValue(objectMapper.writeValueAsString(car),
                new TypeReference<Map<String, String>>(){});

        assertThat(map.get("color")).isEqualTo(car.getColor());
        assertThat(map.get("type")).isEqualTo(car.getType());
    }
}
