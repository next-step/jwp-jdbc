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
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.web.interceptor.ExecutionTimeLogInterceptor;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerExecutor handlerExecutor;
    private HandlerInterceptorRegistry handlerInterceptorRegistry;

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
        handlerInterceptorRegistry.addInterceptor(new ExecutionTimeLogInterceptor());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        Object handler = null;

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(request);
            if (maybeHandler.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            handler = maybeHandler.get();

            if (handlerInterceptorRegistry.applyPreHandle(request, response, handler)) {
                return;
            }

            ModelAndView mav = handlerExecutor.handle(request, response, handler);
            render(mav, request, response);
            handlerInterceptorRegistry.applyPostHandle(request, response, handler, mav);
        } catch (Exception e) {
            triggerAfterCompletion(request, response, handler, e);
        } catch (Throwable e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }

    private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (handler != null) {
            handlerInterceptorRegistry.triggerAfterCompletion(request, response, handler, ex);
        }
    }
}
