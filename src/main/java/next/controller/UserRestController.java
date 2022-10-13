package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        logger.debug("userCreatedDto: {}", userCreatedDto);
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", String.format("/api/users?userId=%s", user.getUserId()));
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam("userId") String userId, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);

        response.setStatus(HttpStatus.OK.value());
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam("userId") String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        user.update(new User(user.getUserId(), user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        response.setStatus(HttpStatus.OK.value());
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
