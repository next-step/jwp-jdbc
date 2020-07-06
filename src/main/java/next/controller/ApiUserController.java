package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

import static core.mvc.tobe.HttpHeader.LOCATION;
import static core.mvc.tobe.HttpStatus.*;

@Controller
public class ApiUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);
    private static final String BASE_URL_USER_API = "/api/users";

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.POST)
    public ModelAndView create(UserCreatedDto userCreatedDto, HttpServletResponse response) {
        User user = userCreatedDto.toEntity();
        DataBase.addUser(user);

        response.setStatus(CREATED);
        response.setHeader(LOCATION, BASE_URL_USER_API + "?userId=" + user.getUserId());
        logger.debug("response : {}", response);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        return modelAndView;
    }

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.GET)
    public ModelAndView retrieve(@RequestParam String userId, HttpServletResponse response) {
        User userById = DataBase.findUserById(userId);
        response.setStatus(OK);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", userById);
        return modelAndView;
    }

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        if (user == null) {
            response.setStatus(BAD_REQUEST);
            return modelAndView;
        }

        response.setStatus(OK);
        User updatedUser = user.update(userUpdatedDto);
        modelAndView.addObject("user", updatedUser);
        return modelAndView;
    }
}
