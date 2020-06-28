package core.mvc;

import core.util.ObjectMapperUtils;
import core.util.PathPatternUtils;
import lombok.Getter;

import java.util.Map;

@Getter
public class PathVariables {
    private Map<String, String> pathVariables;

    public PathVariables(String path, String requestUri) {
        if (path != null) {
            this.pathVariables = PathPatternUtils.getUriVariables(path, requestUri);
        }
    }

    public <T> Object get(String name, Class<T> clazz) {
        return ObjectMapperUtils.convertValue(this.pathVariables.get(name), clazz);
    }
}
