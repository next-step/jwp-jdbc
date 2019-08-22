package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dao.UserDao;
import next.model.User;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserDao userDao = UserDao.getInstance();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = parseUserFromBody(req);

        userDao.insert(user);
        String location = "/api/users?userId=" + user.getUserId();
        resp.setHeader("location", location);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");

        User user = userDao.findByUserId(userId)
                .orElseThrow(NoSuchElementException::new);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("User", user);

        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User updateUser = parseUserFromBody(req);

        User user = userDao.findByUserId(userId)
                .orElseThrow(NoSuchElementException::new);
        user.update(updateUser);
        userDao.update(user);

        return new ModelAndView(new JsonView());
    }

    private User parseUserFromBody(HttpServletRequest req) throws IOException {
        String body = IOUtils.toString(req.getInputStream(), Charset.defaultCharset());
        return JsonUtils.toObject(body, User.class);
    }
}
