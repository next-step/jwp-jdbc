package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.web.interceptor.PerformanceTimerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private InterceptorRegistry interceptorRegistry;

    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new RequestMapping());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);

        interceptorRegistry = new InterceptorRegistry();
        final PerformanceTimerInterceptor pftInterceptor = new PerformanceTimerInterceptor();
        interceptorRegistry.addInterceptor(pftInterceptor);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(req);
            if (maybeHandler.isEmpty()) {
                resp.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            final Object handler = maybeHandler.get();
            preProcess(req, resp, handler);
            ModelAndView mav = handlerExecutor.handle(req, resp, handler);
            postProcess(req, resp, handler);
            render(mav, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void preProcess(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        interceptorRegistry.getInterceptors().forEach(interceptor -> interceptor.preHandle(req, resp, handler));
    }

    private void postProcess(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        interceptorRegistry.getInterceptors().forEach(interceptor -> interceptor.postHandle(req, resp, handler));
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
