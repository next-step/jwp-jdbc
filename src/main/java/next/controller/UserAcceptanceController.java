package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserAcceptanceController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(UserCreatedDto userCreatedDto,
                                   HttpServletResponse response) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);

        response.setStatus(201);
        response.setHeader("location", "/api/users?userId=" + user.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam String userId) {
        User userById = DataBase.findUserById(userId);

        return createUserMav(userById);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(UserUpdatedDto userUpdatedDto, @RequestParam String userId) {
        User userById = DataBase.findUserById(userId);

        userById.update(new User(userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        DataBase.addUser(userById);

        return createUserMav(userById);
    }

    private ModelAndView createUserMav(User user) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        return modelAndView
                .addObject("userId", user.getUserId())
                .addObject("password", user.getPassword())
                .addObject("name", user.getName())
                .addObject("email", user.getEmail());
    }

}
