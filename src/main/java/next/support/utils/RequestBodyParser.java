package next.support.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import next.support.utils.exceptions.RequestBodyParsingException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hspark on 2019-08-18.
 */
public class RequestBodyParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T pasre(HttpServletRequest request, Class<T> clazz) {
        try {
            return MAPPER.readValue(request.getReader(), clazz);
        } catch (IOException e) {
            throw new RequestBodyParsingException(
                    String.format("fail request body parsing, class : %s, request : %s ", clazz.toString(), request.toString())
            );

        }
    }
}
