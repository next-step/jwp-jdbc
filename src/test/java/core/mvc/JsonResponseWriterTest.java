package core.mvc;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonResponseWriterTest {

    @DisplayName("write - model 에 값이 하나일때")
    @Test
    void write() throws Exception {
        Map<String, Object> model = ImmutableMap.of("name", "name!!!");
        Writer writer = new StringWriter();

        JsonResponseWriter.write(writer, model);

        assertThat(writer.toString()).isEqualTo("\"name!!!\"");
    }

    @DisplayName("write - model 에 값이 여러개 일때")
    @Test
    void write1() throws Exception {
        Map<String, Object> model = ImmutableMap.of("name", "name!!!", "count", 5);
        Writer writer = new StringWriter();

        JsonResponseWriter.write(writer, model);

        assertThat(writer.toString()).isEqualTo("{\"name\":\"name!!!\",\"count\":5}");
    }
}