package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserDetailResponseDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req) throws IOException {
        final User user = JsonUtils.toObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())), User.class);

        DataBase.addUser(user);

        return new ModelAndView(new JsonView(HttpServletResponse.SC_CREATED, "/api/users?userId=" + user.getUserId()));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView read(@RequestParam String userId) {
        logger.debug("userId : {}", userId);

        final User user = DataBase.findUserById(userId);
        final UserDetailResponseDto body = new UserDetailResponseDto(user);

        final ModelAndView mav = new ModelAndView(new JsonView(HttpServletResponse.SC_OK));
        mav.addObject("body", body);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, HttpServletRequest req) throws IOException {
        logger.debug("userId : {}", userId);

        final UserUpdatedDto userUpdatedDto = JsonUtils.toObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())), UserUpdatedDto.class);
        final User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new RuntimeException("해당 유저가 없습니다. userId: " + userId);
        }

        DataBase.addUser(new User(userId, user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        final User updatedUser = DataBase.findUserById(userId);

        final ModelAndView mav = new ModelAndView(new JsonView(HttpServletResponse.SC_OK));
        mav.addObject("user", updatedUser);
        return mav;
    }

}
