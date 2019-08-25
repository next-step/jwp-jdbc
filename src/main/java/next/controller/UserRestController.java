package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.RequestReader;
import core.mvc.ResponseWriter;
import next.dto.UserCreatedDto;
import next.dto.UserDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final UserCreatedDto createdDto = RequestReader.fromBody(request, UserCreatedDto.class);

        final User user = new User(
                createdDto.getUserId(),
                createdDto.getPassword(),
                createdDto.getName(),
                createdDto.getEmail());
        logger.debug("User : {}", user);
        DataBase.addUser(user);

        ResponseWriter.created(response, "/api/users?userId=" + user.getUserId());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        final String userId = RequestReader.fromQueryString(request, "userId");

        final User user = DataBase.findUserById(userId);
        final UserDto userDto = new UserDto(user);

        ResponseWriter.ok(response);
        return new ModelAndView(new JsonView())
                .addObject("user", userDto);
    }
}
