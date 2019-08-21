package next.controller;

import core.annotation.web.*;
import core.mvc.view.JsonView;
import core.mvc.view.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/api/users")
public class UserRestController {

    private UserService userService = new UserService();

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws IOException {
        User user = userService.create(userCreatedDto);
        response.setStatus(201);
        response.addHeader("location", "/api/users/" + user.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable(value = "userId") String userId) {
        User user = userService.findByUserId(userId);
        return new ModelAndView(new JsonView()).addObject("user", user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ModelAndView updateUser(@PathVariable String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        userService.update(userId, userUpdatedDto);
        return new ModelAndView(new JsonView());
    }
}
