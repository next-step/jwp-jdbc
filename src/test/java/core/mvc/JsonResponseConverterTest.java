package core.mvc;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonResponseConverterTest {

    @DisplayName("convert - model 에 값이 하나일때")
    @Test
    void convert() throws Exception {
        Map<String, Object> model = ImmutableMap.of("name", "name!!!");

        String result = JsonResponseConverter.convert(model);

        assertThat(result).isEqualTo("\"name!!!\"");
    }

    @DisplayName("convert - model 에 값이 여러개 일때")
    @Test
    void convert1() throws Exception {
        Map<String, Object> model = ImmutableMap.of("name", "name!!!", "count", 5);

        String result = JsonResponseConverter.convert(model);

        assertThat(result).isEqualTo("{\"name\":\"name!!!\",\"count\":5}");
    }
}