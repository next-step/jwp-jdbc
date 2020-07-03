package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.dto.UserCreatedDto;
import next.dto.UserFoundDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

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
        logger.debug("User : {}", user);
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
    public ModelAndView joinUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse resp) throws Exception {
        logger.debug("userCreatedDto.userId : {}", userCreatedDto.getUserId());
        final User user = UserDtoUtils.createdDtoToUser(userCreatedDto);
        DataBase.addUser(user);

        resp.setStatus(HttpStatus.CREATED.value());
        resp.addHeader("Location", "/api/users?userId=" + user.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView userInfo(@RequestParam String userId) throws Exception {
        logger.debug("userId :  {}", userId);
        final User user = DataBase.findUserById(userId);
        final UserFoundDto userFoundDto = UserDtoUtils.userToFoundDto(user);
        final ModelAndView mav = new ModelAndView(new JsonView());

        mav.addObject("foundUser", userFoundDto);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) throws Exception {
        logger.debug("userId : {}, userUpdatedDto.name : {}", userId, userUpdatedDto.getName());
        final User user = DataBase.findUserById(userId);
        final User updatedUser = UserDtoUtils.updateUser(user, userUpdatedDto);
        DataBase.updateUser(updatedUser);

        final ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("updatedUser", UserDtoUtils.userToFoundDto(updatedUser));

        return mav;
    }
}
