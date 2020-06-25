package core.mvc;

import core.mvc.exception.HandlerNotFoundException;
import core.mvc.tobe.interceptor.InterceptorRegistry;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerMappingRegistry {
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public void addHandlerMpping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(hm -> hm.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(HandlerNotFoundException::new);
    }
}
