package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserDetailResponseDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        User user = JsonUtils.toObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())), User.class);

        DataBase.addUser(user);

        ModelAndView mav = new ModelAndView(new JsonView(201, "/api/users?userId=" + user.getUserId()));
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView read(@RequestParam String userId) throws Exception {

        final User user = DataBase.findUserById(userId);
        final UserDetailResponseDto body = new UserDetailResponseDto(user);

        ModelAndView mav = new ModelAndView(new JsonView(200));
        mav.addObject("body", body);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        final User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new RuntimeException("해당 유저가 없습니다. userId: " + userId);
        }

        UserUpdatedDto userUpdatedDto = JsonUtils.toObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())), UserUpdatedDto.class);

        DataBase.addUser(new User(user.getUserId(), user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        ModelAndView mav = new ModelAndView(new JsonView(200));
        mav.addObject("user", DataBase.findUserById(userId));
        return mav;
    }

}
