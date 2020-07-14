package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.interceptor.HandlerInterceptorRegistry;
import core.mvc.interceptor.HandlerInterceptors;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import core.util.BasePackageScanner;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerInterceptorRegistry handlerInterceptorRegistry;
    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        BasePackageScanner basePackageScanner = new BasePackageScanner();
        Object[] basePackage = basePackageScanner.findBasePackage();

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping(basePackage));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerInterceptorRegistry = new HandlerInterceptorRegistry(basePackage);

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestUri = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            Optional<Object> maybeHandler = handlerMappingRegistry.getHandler(request);
            if (!maybeHandler.isPresent()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Object handler = maybeHandler.get();

            HandlerInterceptors interceptors = handlerInterceptorRegistry.findInterceptors(request.getRequestURI());
            if (!interceptors.applyPreHandle(request, response, handler)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);
            interceptors.applyPostHandle(request, response, handler, modelAndView);

            render(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

}
