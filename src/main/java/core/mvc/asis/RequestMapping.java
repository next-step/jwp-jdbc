package core.mvc.asis;

import core.mvc.DispatcherServlet;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import core.mvc.tobe.LegacyHandlerExecution;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    public void initialize() {
        mappings.put("/", new HomeController());
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/updateForm", new UpdateFormUserController());
        mappings.put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        Controller controller = findController(request.getRequestURI());

        if (controller != null) {
            return new LegacyHandlerExecution(controller);
        }

        return null;
    }

    private Controller findController(String requestURI) {
        return mappings.get(requestURI);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
