package next.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        if (br == null) {
            throw new IllegalArgumentException("inputstream is null");
        }
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
