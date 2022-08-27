package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.mvc.tobe.interceptor.TimeTraceInterceptor;
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

    private HandlerInterceptorRegistry handlerInterceptorRegistry;

    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);

        handlerInterceptorRegistry = new HandlerInterceptorRegistry();
        handlerInterceptorRegistry.addInterceptor(new TimeTraceInterceptor());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(req);
        if (maybeHandler.isEmpty()) {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        final Object handler = maybeHandler.get();

        try {
            if (!handlerInterceptorRegistry.applyPreHandle(req, resp, handler)) {
                resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return;
            }

            ModelAndView mav = handlerExecutor.handle(req, resp, handler);

            handlerInterceptorRegistry.applyPostHandle(req, resp, handler);

            render(mav, req, resp);


        } catch (Exception e) {
            applyAfterCompletion(req, resp, handler, e);
        } catch (Throwable e) {
            logger.error("Throwable : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void applyAfterCompletion(final HttpServletRequest req, final HttpServletResponse resp, final Object handler, final Exception e) throws ServletException {
        try {
            handlerInterceptorRegistry.applyAfterCompletion(req, resp, handler, e);
        } catch (Exception ex) {
            logger.error("Exception : {}", e);
            logger.error("Throwable : {}", ex);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
