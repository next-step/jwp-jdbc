package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

@Controller
public class UserRestController {
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);

        response.setStatus(201);
        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView find(@RequestParam("userId") String userId) {
        User user = DataBase.findUserById(userId);

        return new ModelAndView(new JsonView())
                .addObject("user", user);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam("userId") String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());

        return new ModelAndView(new JsonView())
                .addObject("user", user);
    }
}
