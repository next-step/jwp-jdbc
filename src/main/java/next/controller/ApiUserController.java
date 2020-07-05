package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiUserController {
    private static final String BASE_URL_USER_API = "/api/users";

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.POST)
    public ModelAndView create(@RequestBody User user, HttpServletResponse response) {
        DataBase.addUser(user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader(HttpHeaders.LOCATION, BASE_URL_USER_API + "?userId=" + user.getUserId());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.GET)
    public ModelAndView retrieve(@RequestParam String userId, HttpServletResponse response) {
        User userById = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", userById);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        return modelAndView;
    }

    @RequestMapping(value = BASE_URL_USER_API, method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId,
                               UserUpdatedDto userUpdatedDto,
                               HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        if (user == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return modelAndView;
        }

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        User updatedUser = user.update(userUpdatedDto);
        modelAndView.addObject("user", updatedUser);
        return modelAndView;
    }
}
