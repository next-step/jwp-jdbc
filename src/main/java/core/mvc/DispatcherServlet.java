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

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private HandlerExecutor handlerExecutor;

    private InterceptorHandler interceptorHandler;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);

        interceptorHandler = new DefaultInterceptorHandler();
        InterceptorMetaData imd = new DefaultInterceptorMetaData()
                .addInterceptor(new TimeLoggingInterceptor())
                .addPathPatterns("/**");

        interceptorHandler.addInterceptor(imd);


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);
        Exception ex = null;
        Optional<Object> maybeHandler = Optional.empty();

        try {
            maybeHandler = handlerMappingRegistry.getHandler(req);
            if (!maybeHandler.isPresent()) {
                resp.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }

            boolean preHandlePassed = interceptorHandler.preHandle(req, resp, maybeHandler.get());
            if (!preHandlePassed) {
                preHandleFailedLogic(req, resp, maybeHandler.get());
                return;
            }

            ModelAndView mav = handlerExecutor.handle(req, resp, maybeHandler.get());
            interceptorHandler.postHandle(req, resp, maybeHandler.get(), mav);
            render(mav, req, resp);

        } catch (Exception e) {
            logger.error("Exception : {}", e);
            ex = e;
            throw new ServletException(e.getMessage());
        } finally {
            interceptorHandler.afterCompletionHandle(req, resp, maybeHandler.get(), ex);
        }
    }

    private void preHandleFailedLogic(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        JspView jspView = new JspView("redirect:home.jsp");
        render(new ModelAndView(jspView), req, resp);
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
