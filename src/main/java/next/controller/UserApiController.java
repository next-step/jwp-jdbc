package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dao.JdbcUserDao;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.support.utils.RequestBodyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hspark on 2019-08-18.
 */

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final UserDao userDao = JdbcUserDao.INSTANCE;

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView saveUser(HttpServletRequest request, HttpServletResponse response) {
        UserCreatedDto userCreatedDto = RequestBodyParser.pasre(request, UserCreatedDto.class);
        User user = new User(userCreatedDto.getUserId(),
                userCreatedDto.getPassword(),
                userCreatedDto.getName(),
                userCreatedDto.getEmail());
        userDao.insert(user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader(HttpHeaders.LOCATION, "/api/users?userId=" + userCreatedDto.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        User user = userDao.findByUserId(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");

        UserUpdatedDto userUpdatedDto = RequestBodyParser.pasre(request, UserUpdatedDto.class);

        User user = userDao.findByUserId(userId);
        User updatedUser = new User(userId, userUpdatedDto.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail());
        user.update(updatedUser);
        userDao.update(user);

        return new ModelAndView(new JsonView());
    }
}
