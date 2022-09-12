package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private static final String BASE_URI = "/api/users";

    @RequestMapping(value = BASE_URI, method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws IOException {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        logger.debug("User: {}", userCreatedDto);
        DataBase.addUser(user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", "/api/users/" + userCreatedDto.getUserId());

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = BASE_URI + "/{userId}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) throws IOException {
        logger.debug("userId : {}", userId);

        User user = DataBase.findUserById(userId);
        user.update(new User(userId, user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        response.setStatus(HttpStatus.OK.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = BASE_URI +"/{userId}", method = RequestMethod.GET)
    public ModelAndView findUser(@PathVariable String userId, HttpServletResponse response) {
        logger.debug("userId : {}", userId);

        User user = DataBase.findUserById(userId);
        response.setStatus(HttpStatus.OK.value());

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
