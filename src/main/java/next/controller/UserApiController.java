package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.model.User;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by youngjae.havi on 2019-08-17
 */
@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest req, HttpServletResponse resp) {
        User user = DataBase.findUserById(req.getParameter("userId"));
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("User", user);

        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = getUser(req);
        DataBase.addUser(user);
        String location = "/api/users?userId=" + user.getUserId();
        resp.setHeader("location", location);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User updateUser = getUser(req);
        User user = DataBase.findUserById(userId);
        user.update(updateUser);

        return new ModelAndView(new JsonView());
    }

    private User getUser(HttpServletRequest req) throws IOException {
        String body = IOUtils.toString(req.getInputStream(), Charset.defaultCharset());
        return JsonUtils.toObject(body, User.class);
    }
}
