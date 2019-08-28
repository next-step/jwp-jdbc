package next.controller.mvc;

import core.mvc.asis.Controller;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {

    private final UserDao userDao;

    public HomeController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("users", userDao.findAll());
        return "home.jsp";
    }
}
