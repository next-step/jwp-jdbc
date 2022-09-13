package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.interceptor.HandlerInterceptor;
import core.mvc.interceptor.HandlerInterceptorExecutor;
import core.mvc.interceptor.HandlerInterceptorRegistry;
import core.mvc.interceptor.TimeMeasuringInterceptor;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerInterceptorRegistry handlerInterceptorRegistry;
    private HandlerInterceptorExecutor handlerInterceptorExecutor;
    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerInterceptorRegistry = new HandlerInterceptorRegistry();
        handlerInterceptorRegistry
                .addInterceptor(new TimeMeasuringInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/");

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        List<HandlerInterceptor> interceptors = handlerInterceptorRegistry.getMatchedInterceptors(requestUri);
        handlerInterceptorExecutor = new HandlerInterceptorExecutor(interceptors);

        Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(req);
        if (maybeHandler.isEmpty()) {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        Object handler = maybeHandler.get();

        try {
            if (!handlerInterceptorExecutor.applyPreHandle(req, resp, handler)) {
                return;
            }

            ModelAndView mav = handlerExecutor.handle(req, resp, maybeHandler.get());
            handlerInterceptorExecutor.applyPostHandler(req, resp, handler, mav);

            render(mav, req, resp);
            handlerInterceptorExecutor.triggerAfterCompletion(req, resp, handler, null);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            handlerInterceptorExecutor.triggerAfterCompletion(req, resp, handler, (Exception) e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
