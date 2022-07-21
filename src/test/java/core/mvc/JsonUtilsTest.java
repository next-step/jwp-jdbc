package core.mvc;

import next.dto.UserCreatedDto;
import org.junit.jupiter.api.Test;
import study.jackson.Car;

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
    void toObject_UserCreatedDto() throws Exception {
        String json = "{\"userId\":\"pobi\",\"password\":\"password\",\"name\":\"포비\",\"email\":\"pobi@nextstep.camp\"}";
        UserCreatedDto userCreatedDto = JsonUtils.toObject(json, UserCreatedDto.class);
        assertThat(userCreatedDto.getUserId()).isEqualTo("pobi");
    }
}
