package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
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
import java.util.Optional;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerInterceptorRegistry handlerInterceptors = HandlerInterceptorRegistry.defaults();

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;
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
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(request);
            if (maybeHandler.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            handlerInterceptors.preHandle(request, response, maybeHandler);
            ModelAndView mav = handlerExecutor.handle(request, response, maybeHandler.get());
            render(mav, request, response);
            handlerInterceptors.postHandle(request, response, maybeHandler, mav);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }
}
