package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.*;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserDao userDao;

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(
                req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        logger.debug("User : {}", user);
        userDao.insert(user);
        return redirect("/");
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(
                JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return redirect("/users/loginForm");
        }

        ModelAndView mav = new ModelAndView(new JspView("/user/list.jsp"));
        mav.addObject("users", userDao.findAll());
        return mav;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView find(HttpServletRequest request, HttpServletResponse response) throws Exception{
        final User user = userDao.findByUserId(request.getParameter("id"));
        return new ModelAndView(new JsonView()).addObject("user", user);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public ModelAndView put(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final User updateUser = JsonUtils.toObject(request.getInputStream(), User.class);
        userDao.update(updateUser);
        return new ModelAndView(new SuccessView());
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) throws Exception{
        final User user = JsonUtils.toObject(request.getInputStream(), User.class);
        userDao.insert(user);
        return new ModelAndView(new CreatedView("/api/user?id=" + user.getUserId()));
    }

    public void setUserDao(UserDao userDao) {
        logger.debug("{} is injected", userDao.getClass());
        this.userDao = userDao;
    }
}
