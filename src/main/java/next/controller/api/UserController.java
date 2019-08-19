package next.controller.api;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public void test(HttpServletRequest request, HttpServletResponse response) {

    }
}
