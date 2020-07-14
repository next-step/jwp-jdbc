package core.mvc;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class HandlerExecutor {

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }

}
