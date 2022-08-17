package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Objects;

import static core.annotation.web.RequestMethod.GET;
import static core.annotation.web.RequestMethod.POST;
import static core.annotation.web.RequestMethod.PUT;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = POST)
    public ModelAndView addUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        String userId = userCreatedDto.getUserId();
        if (Objects.nonNull(DataBase.findUserById(userId))) {
            throw new IllegalStateException("이미 사용중인 userId입니다.");
        }

        DataBase.addUser(userCreatedDto.toUser());
        response.setStatus(SC_CREATED);
        response.addHeader("location", String.format("/api/users/%s", userId));

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users/{userId}", method = GET)
    public ModelAndView findUser(@PathVariable String userId) {
        User user = getUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{userId}", method = PUT)
    public ModelAndView updateUser(@PathVariable String userId, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = getUserById(userId);
        user.update(userUpdatedDto.toUser());

        return new ModelAndView(new JsonView());
    }

    private User getUserById(String userId) {
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user)) {
            throw new NoSuchElementException("존재하지 않는 userId입니다. userId = [" + userId + "]");
        }
        return user;
    }
}
