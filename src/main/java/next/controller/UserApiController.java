package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.LOCATION;

@Controller
@Slf4j
public class UserApiController {
    public static final String USER_API_URL = "/api/users";
    public static final String HOME_URL = "/";

    @RequestMapping(value = USER_API_URL, method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletResponse response, @RequestBody UserCreatedDto userDto) {
        log.debug("UserCreateDto : {}", StringUtils.toPrettyJson(userDto));

        response.setStatus(HttpStatus.CREATED.value());
        ModelAndView mav = new ModelAndView(new JsonView());

        if (Objects.nonNull(userDto) && userDto.isValid()) {
            User createdUser = User.of(userDto);
            DataBase.addUser(createdUser);
            response.setHeader(LOCATION, USER_API_URL + "?userId=" + createdUser.getUserId());
            mav.addObject("user", userDto);
        }
        else {
            response.setHeader(LOCATION, HOME_URL);
        }

        return mav;
    }

    @RequestMapping(value = USER_API_URL, method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam String userId, HttpServletResponse response) {
        log.debug("userId : {}", userId);

        ModelAndView mav = new ModelAndView(new JsonView());

        User foundUser = DataBase.findUserById(userId);

        if (Objects.nonNull(foundUser)) {
            mav.addObject("user", foundUser);
        }
        else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

        return mav;
    }

    @RequestMapping(value = USER_API_URL, method = RequestMethod.PUT)
    public ModelAndView updateUser(
        @RequestParam String userId,
        @RequestBody UserUpdatedDto userDto,
        HttpServletResponse response
    ) {
        log.debug("userId : {}", userId);
        log.debug("UserUpdateDto : {}", StringUtils.toPrettyJson(userDto));

        ModelAndView mav = new ModelAndView(new JsonView());
        User foundUser = DataBase.findUserById(userId);

        if (Objects.nonNull(foundUser)) {
            foundUser.update(new User(userId, foundUser.getPassword(), userDto.getName(), userDto.getEmail()));
            //mav.addObject("user", foundUser);
        }
        else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

        return mav;
    }
}
