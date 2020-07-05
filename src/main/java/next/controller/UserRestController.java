package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserCreatedDto userCreatedDto = JsonUtils.toObject(request.getInputStream(), UserCreatedDto.class);
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        response.setHeader("Location", String.format("/api/users?userId=%s", user.getUserId()));
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView readUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        response.setHeader("Location", String.format("/api/users?userId=%s", user.getUserId()));
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, HttpServletRequest request) throws IOException {
        User user = Optional.ofNullable(DataBase.findUserById(userId))
                .orElseThrow(() -> new IllegalArgumentException("not found user"));

        UserUpdatedDto userUpdatedDto = JsonUtils.toObject(request.getInputStream(), UserUpdatedDto.class);
        user.update(userUpdatedDto);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

}
