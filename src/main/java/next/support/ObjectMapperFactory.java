package next.support;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperFactory() {
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

}
