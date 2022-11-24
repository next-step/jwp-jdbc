package core.mvc;

import core.web.filter.Interceptor;
import core.web.filter.InterceptorRegistry;
import core.web.filter.LogMeasureInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutor {
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    private final InterceptorRegistry interceptorRegistry;

    public HandlerExecutor(HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;

        this.interceptorRegistry = new InterceptorRegistry();
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        interceptorRegistry.preHandle(request, response, handler);

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        interceptorRegistry.postHandle(request, response, handler, modelAndView);

        return modelAndView;
    }
}
