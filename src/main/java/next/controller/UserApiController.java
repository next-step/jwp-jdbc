package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        UserCreatedDto userCreatedDto = JsonUtils.toObject(content, UserCreatedDto.class);

        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        logger.debug("User: {}", userCreatedDto);
        DataBase.addUser(user);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", "/api/users/" + userCreatedDto.getUserId());
        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        UserUpdatedDto userUpdatedDto = JsonUtils.toObject(content, UserUpdatedDto.class);

        logger.debug("userId : {}", userId);

        User user = DataBase.findUserById(userId);
        user.update(new User(userId, user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        response.setStatus(HttpStatus.OK.value());
        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.GET)
    public ModelAndView findUser(@PathVariable String userId, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        logger.debug("userId : {}", userId);

        User user = DataBase.findUserById(userId);
        modelAndView.addObject("user", user);

        response.setStatus(HttpStatus.OK.value());
        return modelAndView;
    }
}
