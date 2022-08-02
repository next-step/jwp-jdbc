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
public class ApiController {
    private static final String URL = "/api/users";

    @RequestMapping(value = URL, method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam(value = "userId") String userId, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        response.setStatus(HttpServletResponse.SC_OK);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = URL, method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        DataBase.addUser(userCreatedDto.toUser());
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader("Location", URL + "?userId=" + userCreatedDto.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = URL, method = RequestMethod.PUT)
    public ModelAndView updateUser(
            @RequestParam(value = "userId") String userId,
            @RequestBody UserUpdatedDto userUpdatedDto,
            HttpServletResponse response
    ) {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.toUser());
        response.setStatus(HttpServletResponse.SC_OK);
        return new ModelAndView(new JsonView());
    }
}
