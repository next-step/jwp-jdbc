package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Controller
public class UserApiController {

  @RequestMapping(value = "/api/users", method = RequestMethod.POST)
  public ModelAndView crate(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserCreatedDto newUser = JsonUtils.toObject(getBody(request), UserCreatedDto.class);
    UserService userService = new UserService();
    User createdUser = userService.createUser(newUser);
    response.setStatus(HttpStatus.CREATED.value());
    response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + createdUser.getUserId());
    return new ModelAndView(new JsonView());
  }

  private String getBody(HttpServletRequest request) throws IOException {
    return request.getReader().lines()
        .collect(Collectors.joining(System.lineSeparator()));
  }

  @RequestMapping(value = "/api/users", method = RequestMethod.GET)
  public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserService userService = new UserService();
    User user = userService.getUser(request.getParameter("userId"));
    response.setStatus(HttpStatus.OK.value());
    ModelAndView modelAndView = new ModelAndView(new JsonView());
    modelAndView.addObject("user", user);
    return modelAndView;
  }

  @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
  public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserService userService = new UserService();
    UserUpdatedDto updatedDto = JsonUtils.toObject(getBody(request), UserUpdatedDto.class);
    userService.updateUser(request.getParameter("userId"), updatedDto);
    response.setStatus(HttpStatus.OK.value());
    return new ModelAndView(new JsonView());
  }


}
