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
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        DataBase.addUser(userCreatedDto.toUser());

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + userCreatedDto.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView find(@RequestParam String userId) {
        final User user = DataBase.findUserById(userId);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView modify(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        final User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.toUser());
        DataBase.addUser(user);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
