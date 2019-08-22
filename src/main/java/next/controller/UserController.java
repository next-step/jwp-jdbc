package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.view.JspView;
import core.mvc.view.ModelAndView;
import core.mvc.view.RedirectView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView users(HttpSession session) {
        if (!UserSessionUtils.isLogined(session)) {
            RedirectView redirectView = new RedirectView("redirect:/users/loginForm");
            return new ModelAndView(redirectView);
        }

        JspView jspView = new JspView("/user/list");
        ModelAndView mav = new ModelAndView(jspView);
        mav.addObject("users", DataBase.findAll());
        return mav;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public ModelAndView userForm() {
        return new ModelAndView(new JspView("/user/form"));
    }

    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm() {
        return new ModelAndView(new JspView("/user/login"));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);

        DataBase.addUser(user);
        return new ModelAndView(new RedirectView("redirect:/"));
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView showProfile(@RequestParam String userId) {
        User user = DataBase.findUserById(userId);

        ModelAndView mav = new ModelAndView();

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        mav.addView(new JspView("/user/profile"));
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpSession session, String userId) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        return new ModelAndView(new JspView("/user/updateForm")).addObject("user", user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(HttpSession session, String userId, String password, String name, String email) {
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(session, user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(userId, password, name, email);
        log.debug("Update User : {}", updateUser);

        user.update(updateUser);

        return new ModelAndView(new RedirectView("redirect:/"));
    }
}
