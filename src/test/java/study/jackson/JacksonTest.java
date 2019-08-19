package study.jackson;

import core.mvc.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JacksonTest {
    @DisplayName("one element")
    @Test
    void oneElement() {
        Map<String, Object> model = new HashMap<>();
        model.put("car", new Car("black", "Santafe"));

        String key = model.keySet()
                .stream()
                .findFirst()
                .orElseThrow(NullPointerException::new);

        System.out.println(key);
    }

    @DisplayName("no element")
    @Test
    void noElement() {
        Map<String, Object> model = null;
        System.out.println(JsonUtils.toJson(model));
    }

    @DisplayName("empty element")
    @Test
    void emptyElement() {
        Map<String, Object> model = new HashMap<>();
        System.out.println(JsonUtils.toJson(model));
    }
}
