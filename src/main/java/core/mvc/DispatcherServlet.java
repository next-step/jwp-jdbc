package core.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import core.exception.EntityNotFoundExceptionHandler;
import core.exception.ExceptionHandlers;
import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.mvc.asis.ControllerHandlerAdapter;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.BeanRegistry;
import core.mvc.tobe.HandlerExecutionHandlerAdapter;
import next.dao.UserDao;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;

    private HandlerAdapterRegistry handlerAdapterRegistry;

    private HandlerExecutor handlerExecutor;

    private ExceptionHandlers exceptionHandlers;

    @Override
    public void init() {
        BeanRegistry.addBean(JdbcTemplate.class, new JdbcTemplate(ConnectionManager.getDataSource()));
        BeanRegistry.addBean(UserDao.class, new UserDao(BeanRegistry.getBean(JdbcTemplate.class)));

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMpping(new RequestMapping());
        handlerMappingRegistry.addHandlerMpping(new AnnotationHandlerMapping("next.controller"));

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);

        MessageConverters messageConverters = MessageConverters.getInstance();
        messageConverters.add(new JsonMessageConverter());

        exceptionHandlers = new ExceptionHandlers();
        exceptionHandlers.addHandler(new EntityNotFoundExceptionHandler());
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
            handleServiceException(e, req, resp);
        }
    }

    private void handleServiceException(Throwable e, HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        Throwable actualException = e;

        if (e instanceof InvocationTargetException) {
            actualException = ((InvocationTargetException) e).getTargetException();
        }

        if (exceptionHandlers.supports(actualException.getClass())) {
            exceptionHandlers.handle(actualException, req, resp);
            return;
        }

        logger.error("Exception : {}", actualException);
        throw new ServletException(actualException.getMessage());
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }
}
