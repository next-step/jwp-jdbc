package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.LOCATION;

@RequestMapping("/api/users")
@Slf4j
public class UserApiController {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String USER_API_URL = "/api/users";
    public static final String HOME_URL = "/";

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createUser(HttpServletResponse response, UserCreatedDto userDto) {
        log.debug("UserCreateDto : {}", StringUtils.toPrettyJson(userDto));

        ModelAndView mav = new ModelAndView(new JsonView());

        if (Objects.nonNull(userDto) && userDto.isValid()) {
            User createdUser = User.of(userDto);
            DataBase.addUser(createdUser);
            response.setHeader(LOCATION, USER_API_URL + "?userId=" + createdUser.getUserId());
            mav.addObject("user", createdUser);
        }
        else {
            response.setHeader(LOCATION, HOME_URL);
        }

        return mav;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findUser(String userId) {
        log.debug("userId : {}", userId);

        ModelAndView mav = new ModelAndView(new JsonView());

        User foundUser = DataBase.findUserById(userId);

        if (Objects.nonNull(foundUser)) {
            mav.addObject("user", foundUser);
        }

        return mav;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ModelAndView updateUser(String userId, UserUpdatedDto userDto) {
        log.debug("userId : {}", userId);
        log.debug("UserUpdateDto : {}", StringUtils.toPrettyJson(userDto));

        ModelAndView mav = new ModelAndView(new JsonView());
        User foundUser = DataBase.findUserById(userId);

        if (Objects.nonNull(foundUser)) {
            foundUser.update(new User(userId, foundUser.getPassword(), userDto.getName(), userDto.getEmail()));
            mav.addObject("user", foundUser);
        }

        return mav;
    }
}
