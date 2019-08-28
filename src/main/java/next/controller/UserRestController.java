package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.RequestReader;
import next.dto.UserCreatedDto;
import next.dto.UserDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(
            final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final UserCreatedDto createdDto = RequestReader.fromBody(request, UserCreatedDto.class);

        final User user = createdDto.toUser();
        logger.debug("User : {}", user);
        DataBase.addUser(user);

        final JsonView jsonView = new JsonView(HttpStatus.CREATED, URI.create("/api/users?userId=" + user.getUserId()));
        return new ModelAndView(jsonView);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(final HttpServletRequest request, final HttpServletResponse response) {
        final String userId = RequestReader.fromQueryString(request, "userId");

        final User user = DataBase.findUserById(userId);
        final UserDto userDto = UserDto.from(user);

        return new ModelAndView(new JsonView(HttpStatus.OK))
                .addObject("user", userDto);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String userId = RequestReader.fromQueryString(request, "userId");
        final UserUpdatedDto updatedDto = RequestReader.fromBody(request, UserUpdatedDto.class);

        final User user = DataBase.findUserById(userId);
        final User updatedUser = updatedDto.toUser(user);
        user.update(updatedUser);

        return new ModelAndView(new JsonView(HttpStatus.OK));
    }
}
