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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@Controller
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView apiUsers(@RequestBody UserCreatedDto userCreatedDto) {
        User user = userCreatedDto.toUser();
        DataBase.addUser(user);
        return createdJson("/api/users?userId=" + user.getUserId());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView apiUsers(@RequestParam String userId) {
        User user = DataBase.findUserById(userId);
        ModelAndView modelAndView = okJson();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView apiUsers(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User foundUser = DataBase.findUserById(userId);
        DataBase.addUser(new User(userId, foundUser.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));
        ModelAndView modelAndView = okJson();
        return modelAndView;
    }

    private ModelAndView okJson() {
        return new ModelAndView(new JsonView());
    }

    private ModelAndView createdJson(String location) {
        return new ModelAndView(new JsonView(location, HttpStatus.CREATED));
    }

}
