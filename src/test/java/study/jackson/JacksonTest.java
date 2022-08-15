package study.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("jackson 라이브러리 익히기")
class JacksonTest {

    @Test
    @DisplayName("json 변환을 위한 객체 생성")
    void instance() {
        assertThatNoException().isThrownBy(ObjectMapper::new);
    }

    @Test
    @DisplayName("car 변환해보기")
    void writeValueAsString() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        Car redBus = new Car("red", "bus");
        //when
        String redBusJson = mapper.writeValueAsString(redBus);
        //then
        assertThat(redBusJson).isEqualTo("{\"color\":\"red\",\"type\":\"bus\"}");
    }

    @Test
    @DisplayName("json car 객체로 변환해보기")
    void readValue() throws JsonProcessingException {
        //given
        ObjectMapper mapper = new ObjectMapper();
        //when
        Car car = mapper.readValue("{\"color\":\"red\",\"type\":\"bus\"}", Car.class);
        //then
        assertThat(car).isEqualTo(new Car("red", "bus"));
    }
}
