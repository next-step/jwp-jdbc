package next.controller;

import core.db.DataBase;
import core.mvc.asis.Controller;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", new UserDao().findAll());
        return "home.jsp";
    }
}
