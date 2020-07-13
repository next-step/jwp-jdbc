package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;


import javax.servlet.http.HttpServletResponse;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserApiController {
    private static final Logger logger = getLogger(UserApiController.class);
    public static final String API_USERS_BASE_URL = "/api/users";

    @RequestMapping(value = API_USERS_BASE_URL, method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        logger.debug("userCreatedDto: {}", userCreatedDto.toString());
        User user = userCreatedDto.toUser();
        DataBase.addUser(user);

        response.setHeader("Location", API_USERS_BASE_URL + "?userId=" + user.getUserId());
        response.setStatus(HttpStatus.CREATED.value());

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        return modelAndView;
    }

    @RequestMapping(value = API_USERS_BASE_URL)
    public ModelAndView show(@RequestParam String userId) {
        User user = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = API_USERS_BASE_URL, method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.toUpdateUser());

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        return modelAndView;
    }
}
