package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiUserController {
    private static final String BASE_URL_USER_API = "/api/users";

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.POST)
    public ModelAndView create(@RequestBody User user, HttpServletResponse response) {
        DataBase.addUser(user);

        response.setStatus(201);
        response.setHeader(HttpHeaders.LOCATION, BASE_URL_USER_API + "?userId=" + user.getUserId());
        return new ModelAndView(new JsonView());
    }
}
