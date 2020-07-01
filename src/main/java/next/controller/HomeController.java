package next.controller;

import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Handler {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView(new JspView("home.jsp"));
    }

    @Override
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
}
