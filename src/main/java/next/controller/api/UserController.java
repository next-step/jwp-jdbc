package next.controller.api;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.HttpServletRequestUtils;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;
import next.service.UserService;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Controller
public class UserController {

    private static final UserService userService = new UserService();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        final String requestBody = HttpServletRequestUtils.getRequestBody(request);
        final UserCreatedDto userCreatedDto = JsonUtils.toObject(requestBody, UserCreatedDto.class);

        final User user = userService.create(userCreatedDto);
        final String location = "/api/users?userId=" + user.getUserId();

        return new ModelAndView(new JsonView(HttpStatus.CREATED, location));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        final String userId = request.getParameter("userId");

        final User user = userService.findByUserId(userId);
        final String location = "/api/users?userId=" + user.getUserId();

        return new ModelAndView(new JsonView(location))
                .addObject("user", user);
    }
}
