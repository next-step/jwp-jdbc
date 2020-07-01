package core.mvc;

import core.interceptor.InterceptorRegistry;
import core.mvc.asis.LegacyHandlerMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.Handler;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CONTROLLER_PATH = "next.controller";

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private static final InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    private AnnotationHandlerMapping mapping;
    private LegacyHandlerMapping rm;

    @Override
    public void init() {
        handlerMappings.add(new LegacyHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping(CONTROLLER_PATH));
    }

    @SneakyThrows
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = getHandler(req);
        ModelAndView modelAndView;

        Throwable throwable = null;
        try {
            boolean preHandleResult = interceptorRegistry.preHandle(req, resp, handler);
            if (!preHandleResult) {
                return;
            }

            modelAndView = ((Handler) handler).handle(req, resp);
            interceptorRegistry.postHandle(req, resp, handler, modelAndView);
            modelAndView.render(req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throwable = e;
            throw new ServletException(e.getMessage());
        } finally {
            interceptorRegistry.afterCompletion(req, resp, handler, (Exception) throwable);
        }
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(req);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }
        return null;
    }
}
