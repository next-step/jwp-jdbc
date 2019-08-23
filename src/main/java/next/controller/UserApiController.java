package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.RequestBodyParser;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserCreatedDto created = RequestBodyParser.parse(req, UserCreatedDto.class);

        User newUser = created.toEntity();
        DataBase.addUser(newUser);

        setCreatedStatus(newUser, resp);
        return new ModelAndView(new JsonView());
    }

    private void setCreatedStatus(User user, HttpServletResponse resp) {
        resp.setStatus(201);
        resp.setHeader("Location", "/api/users?userId=" + user.getUserId());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = findByUserId(req);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserUpdatedDto updated = RequestBodyParser.parse(req, UserUpdatedDto.class);

        User user = findByUserId(req);
        user.update(updated.toEntityWith(user));

        return new ModelAndView(new JsonView());
    }

    private User findByUserId(HttpServletRequest req) {
        String userId = req.getParameter("userId");
        return DataBase.findUserById(userId);
    }
}
