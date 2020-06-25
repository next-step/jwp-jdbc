package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.exception.HandlerNotFoundException;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.mvc.tobe.interceptor.ApiElapsedTimeInterceptor;
import core.mvc.tobe.interceptor.InterceptorChain;
import core.mvc.tobe.interceptor.InterceptorRegistry;
import core.mvc.tobe.interceptor.UserFormInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private InterceptorRegistry interceptorRegistry;

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        interceptorRegistry = new InterceptorRegistry()
            .addInterceptor(new ApiElapsedTimeInterceptor())
            .addInterceptor(new UserFormInterceptor("/users/loginForm", "/users/form"));

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Object handler = null;
        InterceptorChain interceptorChain = new InterceptorChain(interceptorRegistry.getMatchedInterceptors(requestUri));

        try {
            handler = handlerMappingRegistry.getHandler(req);
            if (!interceptorChain.applyPreHandle(req, resp, handler)) {
                return;
            }

            ModelAndView mav = handlerExecutor.handle(req, resp, handler);

            interceptorChain.applyPostHandle(req, resp, handler, mav);
            render(mav, req, resp);
            interceptorChain.applyAfterCompletion(req, resp, handler, null);
        }
        catch (HandlerNotFoundException e) {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        catch (Throwable e) {
            logger.error("Exception : {}", e);
            ServletException exception = new ServletException(e.getMessage());
            interceptorChain.applyAfterCompletion(req, resp, handler, exception);
            throw exception;
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
