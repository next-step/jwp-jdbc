package core.mvc;

import core.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry(WebApplicationContext webApplicationContext) {
        handlerAdapters.addAll(webApplicationContext.getHandlerAdapters());
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(ha -> ha.supports(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
