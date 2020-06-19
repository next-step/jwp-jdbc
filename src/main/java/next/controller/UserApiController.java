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
public class UserApiController {
    public static final String API_USERS = "/api/users";

    @RequestMapping(value = API_USERS, method = RequestMethod.POST)
    public ModelAndView creatUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        DataBase.addUser(userCreatedDto.toUser());

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader("location", "/api/users?userId=" + userCreatedDto.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam(value = "userId") String userId, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);

        response.setStatus(HttpServletResponse.SC_OK);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam(value = "userId") String userId,
                                   @RequestBody UserUpdatedDto userUpdatedDto,
                                   HttpServletResponse response) {
        System.out.println(userUpdatedDto);
        System.out.println(userUpdatedDto.getEmail());
        System.out.println(userUpdatedDto.getName());
        User user = DataBase.findUserById(userId);
        System.out.println(user);
        user.update(userUpdatedDto.toUser());
        System.out.println(user);

        response.setStatus(HttpServletResponse.SC_OK);
        return new ModelAndView(new JsonView());
    }
}
