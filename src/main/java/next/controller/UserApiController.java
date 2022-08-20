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
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class UserApiController {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private final UserDao userDao = new UserDao();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        UserCreatedDto userCreatedDto = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);
        userDao.insert(userCreatedDto.toUser());
        response.setHeader("location", "/api/users?userId=" + userCreatedDto.getUserId());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        User findUser = getUser(request);
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", findUser);
        return modelAndView;
    }

    private User getUser(HttpServletRequest request) throws SQLException {
        String userId = request.getParameter("userId");
        return userDao.findByUserId(userId);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        User findUser = getUser(request);
        UserUpdatedDto userUpdatedDto = objectMapper.readValue(request.getInputStream(), UserUpdatedDto.class);
        findUser.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());
        userDao.update(findUser);

        return new ModelAndView(new JsonView());
    }
}
