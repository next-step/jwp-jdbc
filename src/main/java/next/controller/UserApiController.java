package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import next.dto.UserUpdatedDto;
import next.model.User;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody User user, HttpServletResponse response) {
        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView profile(@RequestParam String userId) {
        final User user = DataBase.findUserById(userId);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        final User user = DataBase.findUserById(userId);

        final User updateUser = new User(user.getUserId(), user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail());

        user.update(updateUser);

        return new ModelAndView(new JsonView());
    }

}
