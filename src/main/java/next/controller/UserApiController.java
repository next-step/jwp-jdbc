package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.ResponseHeaderSetter;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);
    private static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

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
        ResponseHeaderSetter.setStatusCREATED(response, location);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users")
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");

        User user = userService.getUser(userId);

        log.debug("user : {}", user);
        ResponseHeaderSetter.setStatusOK(response);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userId = request.getParameter("userId");
        UserUpdatedDto updatedDto = JsonUtils.toObject(request.getReader(), UserUpdatedDto.class);

        userService.updateUser(userId, updatedDto);
        ResponseHeaderSetter.setStatusOK(response);
        return new ModelAndView(new JsonView());
    }

}
