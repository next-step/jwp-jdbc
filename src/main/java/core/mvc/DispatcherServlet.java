package core.mvc;

import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import java.util.Locale;
import java.util.Map;
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
import org.springframework.lang.Nullable;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private ViewResolverComposite viewResolverComposite;

    private HandlerExecutor handlerExecutor;

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        viewResolverComposite = new ViewResolverComposite();
        viewResolverComposite.addViewResolver(new JsonViewResolver());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
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


            ModelAndView mav = handlerExecutor.handle(req, resp, maybeHandler.get());
            render(mav, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = null;

        String viewName = mav.getViewName();
        if (viewName != null) {
            view = resolveViewName(viewName);
        }

        if (view == null) {
            view = mav.getView();
        }
        
        if (view == null) {
            throw new ServletException("Cannot resolve view");
        }
        
        try {
            if (mav.getStatus() != null) {
                resp.setStatus(mav.getStatus().value());
            }
            
            view.render(mav.getModel(), req, resp);
            
        } catch (Exception e) {
            logger.debug("Error rendering view [" + view + "]", e);    
            throw e;
        }
        
    }

    private View resolveViewName(String viewName) {
        if (viewResolverComposite != null) {
            return this.viewResolverComposite.resolveViewName(viewName);
        }
        return null;
    }

}
