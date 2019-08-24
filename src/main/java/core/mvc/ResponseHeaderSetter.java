package core.mvc;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

public class ResponseHeaderSetter {

    public static void setStatusOK(HttpServletResponse response) {
        setResponseHeaders(response, HttpStatus.OK, Collections.emptyMap());
    }

    public static void setStatusCREATED(HttpServletResponse response, String location) {
        setResponseHeaders(response, HttpStatus.CREATED, ImmutableMap.of(HttpHeaders.LOCATION, location));
    }

    private static void setResponseHeaders(HttpServletResponse response, HttpStatus httpStatus, Map<String, String> headers) {
        response.setStatus(httpStatus.value());
        headers.forEach(response::setHeader);
    }

}
