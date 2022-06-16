package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static org.springframework.http.HttpHeaders.LOCATION;

@Controller
public class ApiUserController {
    private final UserDao userDao = new UserDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserCreatedDto createdDto = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);

        userDao.insert(createdDto.toUser());

        response.setHeader(LOCATION, "/api/users?userId=" + createdDto.getUserId());
        response.setStatus(HttpServletResponse.SC_CREATED);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = findUserByUserId(request);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = findUserByUserId(request);

        UserUpdatedDto updatedUser = objectMapper.readValue(request.getInputStream(), UserUpdatedDto.class);
        user.update(updatedUser.getName(), updatedUser.getEmail());
        userDao.update(user);

        return new ModelAndView(new JsonView());
    }

    private User findUserByUserId(HttpServletRequest request) throws SQLException {
        String userId = request.getParameter("userId");
        return userDao.findByUserId(userId);
    }
}
