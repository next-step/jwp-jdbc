package next.controller;

import com.google.common.net.HttpHeaders;
import core.annotation.RequestBody;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    private final UserDao userDao = new UserDao();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        userDao.insert(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + user.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam String userId) {
        return userModelAndView(userDao.findByUserId(userId));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = userDao.findByUserId(userId);
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());
        userDao.update(user);
        return userModelAndView(user);
    }

    private ModelAndView userModelAndView(User user) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
