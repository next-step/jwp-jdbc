package next.controller;

import core.jdbc.JdbcTemplate;
import core.mvc.asis.Controller;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {

    private static UserDao userDao = new UserDao();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //req.setAttribute("users", DataBase.findAll());
        req.setAttribute("users", userDao.findAll());
        return "home.jsp";
    }
}
