package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("userId", user.getUserId());

        response.setStatus(HttpStatus.CREATED.value());
        response.addHeader("Location", "/api/users?userId=" + user.getUserId());
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView readUser(@RequestParam String userId) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = DataBase.findUserById(userId);
        User updateUser = new User(userId, user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail());
        user.update(updateUser);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
