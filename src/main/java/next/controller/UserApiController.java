package next.controller;

import static core.mvc.JsonView.JSON_VIEW_PREFIX;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpStatus;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView apiUsers(@RequestBody UserCreatedDto userCreatedDto) {
        User user = userCreatedDto.toUser();
        DataBase.addUser(user);
        ModelAndView modelAndView = new ModelAndView(JSON_VIEW_PREFIX +"/api/users?userId=" + user.getUserId(), HttpStatus.CREATED);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView apiUsers(@RequestParam String userId) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = new ModelAndView(JSON_VIEW_PREFIX);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView apiUsers(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User foundUser = DataBase.findUserById(userId);
        DataBase.addUser(new User(userId, foundUser.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));
        return new ModelAndView(JSON_VIEW_PREFIX);
    }

}
