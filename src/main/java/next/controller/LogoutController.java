package next.controller;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController implements Handler {
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    @Override
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
}
