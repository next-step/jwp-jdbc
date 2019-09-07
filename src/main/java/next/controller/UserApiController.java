package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.view.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserApiController {

    private UserService userService;
    private ObjectMapper objectMapper;

    public UserApiController() {
        userService = new UserService();
        objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView crate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserCreatedDto newUser = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);
        User createdUser = userService.createUser(newUser);
        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + createdUser.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUser(request.getParameter("userId"));
        response.setStatus(HttpStatus.OK.value());
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        UserUpdatedDto updatedDto = objectMapper.readValue(request.getInputStream(), UserUpdatedDto.class);
        userService.updateUser(request.getParameter("userId"), updatedDto);
        response.setStatus(HttpStatus.OK.value());
        return new ModelAndView(new JsonView());
    }


}
