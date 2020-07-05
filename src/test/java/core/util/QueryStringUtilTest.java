package core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class QueryStringUtilTest {

    @DisplayName("QueryString 생성하기")
    @Test
    void generateQueryString() {
        /* given */
        String uri = "/api/users";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        parameters.put("key2", "value2");

        /* when */
        String queryString = QueryStringUtil.generateQueryStringWithUri(uri, parameters);

        /* then */
        assertThat(queryString).isEqualTo("/api/users?key1=value1&key2=value2");
    }
}
