package next.controller;

import com.google.common.net.HttpHeaders;
import core.annotation.RequestBody;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + user.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam String userId) {
        return userModelAndView(DataBase.findUserById(userId));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());
        return userModelAndView(user);
    }

    private ModelAndView userModelAndView(User user) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
