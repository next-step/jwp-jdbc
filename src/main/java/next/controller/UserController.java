package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam String userId) throws Exception {
        logger.debug("userId : {}", userId);
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }
        ModelAndView mav = new ModelAndView(new JspView("/user/profile.jsp"));
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView create(User user) throws Exception {
        logger.debug("create User : {}", user);
        DataBase.addUser(user);
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
        mav.addObject("users", DataBase.findAll());
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView join(UserCreatedDto userCreatedDto, HttpServletResponse resp) throws Exception {
        User user = userCreatedDto.toUser();
        logger.debug("join User : {}", user);
        DataBase.addUser(user);

        resp.setStatus(HttpStatus.CREATED.value());
        resp.addHeader("Location", "/api/users/" + user.getUserId());

        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.GET)
    public ModelAndView find(@PathVariable String userId, HttpServletResponse resp) throws Exception {
        logger.debug("userId : {}", userId);
        User user = DataBase.findUserById(userId);
        DataBase.addUser(user);

        resp.setStatus(HttpStatus.OK.value());

        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PUT)
    public ModelAndView put(@PathVariable String userId, UserUpdatedDto userUpdatedDto, HttpServletResponse resp) throws Exception {
        logger.debug("userUpdatedDto : {}", userUpdatedDto);
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());
        DataBase.addUser(user);

        resp.setStatus(HttpStatus.OK.value());

        ModelAndView mav = new ModelAndView(new JsonView());
        return mav;
    }
}
