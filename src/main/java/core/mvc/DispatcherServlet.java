package core.mvc;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.interceptor.InterceptorExecutor;
import core.mvc.interceptor.InterceptorRegistry;
import core.mvc.interceptor.LoggerInterceptor;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private HandlerExecutor handlerExecutor;

    private InterceptorRegistry interceptorRegistry;

    private InterceptorExecutor interceptorExecutor;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        interceptorRegistry = new InterceptorRegistry();
        interceptorRegistry.addInterceptor(new LoggerInterceptor());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
        interceptorExecutor = new InterceptorExecutor(interceptorRegistry);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(req);
            if (!maybeHandler.isPresent()) {
                resp.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            var handler = maybeHandler.get();

            var modelAndView = interceptorExecutor.handle(
                () -> requestHandle(req, resp, handler), req, resp, handler
            );

            render(req, resp, modelAndView);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView requestHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        try {
            return handlerExecutor.handle(req, resp, handler);
        } catch (Exception e) {
            throw new IllegalArgumentException("requestHandle exception >>>" + e.getMessage());
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
