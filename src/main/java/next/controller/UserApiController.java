package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.converter.UserConverter;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import next.utils.RequestBodyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserApiController {
    private static final String USER_API_BASE_PATH = "/api/users";
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = USER_API_BASE_PATH, method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto createdDto = (UserCreatedDto) RequestBodyUtils.toObject(request, UserCreatedDto.class);
        logger.debug("Create User : {}", createdDto);
        userService.createUser(createdDto);

        String location = USER_API_BASE_PATH + "?userId=" + createdDto.getUserId();
        response.setHeader("Location", location);
        response.setStatus(HttpStatus.CREATED.value());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = USER_API_BASE_PATH, method = RequestMethod.GET)
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = userService.findBy(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = USER_API_BASE_PATH, method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        UserUpdatedDto updatedDto = (UserUpdatedDto) RequestBodyUtils.toObject(request, UserUpdatedDto.class);

        userService.update(userId, updatedDto);

        return new ModelAndView(new JsonView());
    }
}
