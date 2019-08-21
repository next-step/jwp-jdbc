package next.controller;

import com.google.common.collect.ImmutableMap;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final UserService userService;

    public UserApiController() {
        userService = new UserService();
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserCreatedDto createdDto = JsonUtils.toObject(request.getReader(), UserCreatedDto.class);

        log.debug("createDto : {}", createdDto);

        userService.createUser(createdDto);

        String location = "/api/users?userId=" + createdDto.getUserId();
        return new ModelAndView(JsonView.created(ImmutableMap.of(HttpHeaders.LOCATION, location)));
    }

    @RequestMapping(value = "/api/users")
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");

        User user = userService.getUser(userId);

        log.debug("user : {}", user);
        ModelAndView modelAndView = new ModelAndView(JsonView.ok());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        UserUpdatedDto updatedDto = JsonUtils.toObject(request.getReader(), UserUpdatedDto.class);

        userService.updateUser(userId, updatedDto);
        return new ModelAndView(JsonView.ok());
    }

}
