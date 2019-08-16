package study;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaAPITest {

    @Test
    void findMapNull() {
        Map<String, String> map = new HashMap<>();
        String result = map.get(null);
        assertThat(result).isEqualTo(null);
    }

}
