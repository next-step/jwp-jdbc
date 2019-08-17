package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserAcceptanceController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView userQuery(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("User : {}", req.getParameter("userId"));

        User user = DataBase.findUserById(req.getParameter("userId"));
        return new ModelAndView(new JsonView())
                .addObject("user", user);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView userCreate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User newUser = JsonUtils.getParamterClass(request, User.class);

        DataBase.addUser(newUser);

        response.setHeader("location", "/api/users?userId=" + newUser.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView userModify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User modifyUser = JsonUtils.getParamterClass(request, User.class);

        DataBase.modifyUser(request.getParameter("userId"), modifyUser);

        response.setHeader("location", "/api/users?userId=" + modifyUser.getUserId());
        return new ModelAndView(new JsonView());
    }




}
