package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.service.UserService;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class UserAcceptanceController {
    private static final Logger logger = LoggerFactory.getLogger(UserAcceptanceController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        UserCreatedDto userCreatedDto = objectMapper.readValue(request.getReader(), UserCreatedDto.class);
        UserService.addUser(userCreatedDto);

        response.addHeader("location", "/api/users?userId=" + userCreatedDto.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView retrieveUser(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String userId = request.getParameter("userId");
        User user = UserService.findUserById(userId);

        response.setStatus(HttpStatus.OK.value());
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String userId = request.getParameter("userId");
        UserUpdatedDto userUpdatedDto = objectMapper.readValue(request.getReader(), UserUpdatedDto.class);
        UserService.updateUser(userId, userUpdatedDto);

        response.setStatus(HttpStatus.OK.value());
        return new ModelAndView(new JsonView());
    }
}