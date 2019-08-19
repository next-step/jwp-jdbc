package next.utils;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest {

    @Test
    void readData() throws IOException {
        String data = "abcde";
        StringReader stringReader = new StringReader(data);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        assertThat(IOUtils.readData(bufferedReader, data.length())).isEqualTo("abcde");
    }

    @Test
    void readData_throw_illegalArguementException() {
        assertThrows(IllegalArgumentException.class, () -> {
            IOUtils.readData(null, 0);
        });
    }
}