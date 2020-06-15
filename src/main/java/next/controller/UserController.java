package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.mvc.ModelAndView;
import core.mvc.tobe.AbstractNewController;
import next.dao.UserDao;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserDao userDao = UserDao.getInstance();

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        ModelAndView mav = jspView("/user/list.jsp");
        mav.addObject("users", userDao.findAll());
        return mav;
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam String userId) throws Exception {
        ModelAndView mav = jspView("/user/profile.jsp");
        mav.addObject("user", userDao.findByUserId(userId));
        return mav;
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form() throws Exception {
        return jspView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest request) throws Exception {
        User user = userDao.findByUserId(request.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        ModelAndView mav = jspView("/user/updateForm.jsp");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(UserUpdatedDto updateUser, HttpServletRequest request) throws Exception {
        User user = userDao.findByUserId(updateUser.getUserId());

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        log.debug("Update User : {}", updateUser);
        user.update(updateUser);
        userDao.update(user);
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create(User user) throws Exception {
        log.debug("User : {}", user);
        userDao.insert(user);
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm() throws Exception {
        return jspView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) throws Exception {
        User user = userDao.findByUserId(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return jspView("redirect:/");
        } else {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return jspView("redirect:/");
    }
}
