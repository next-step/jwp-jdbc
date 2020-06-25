package core.mvc.asis;

import core.mvc.DispatcherServlet;
import core.mvc.HandlerMapping;
import next.controller.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = new HashMap<>();

    @Override
    public void initialize() {
        put("/", new HomeController());
        put("/users/form", new ForwardController("/user/form.jsp"));
        put("/users/loginForm", new ForwardController("/user/login.jsp"));
        put("/users/login", new LoginController());
        put("/users/logout", new LogoutController());
        put("/users/updateForm", new UpdateFormUserController());
        put("/users/update", new UpdateUserController());

        logger.info("Initialized Request Mapping!");
        mappings.keySet().forEach(path -> {
            logger.info("Path : {}, Controller : {}", path, mappings.get(path).getClass());
        });
    }

    public Object getHandler(HttpServletRequest request) {
        return mappings.get(request.getRequestURI());
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
