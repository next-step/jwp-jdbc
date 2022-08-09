package next.controller;

import com.google.common.net.HttpHeaders;
import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.UserNotFoundException;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    private static final String BASE_URL = "/api/users";

    @RequestMapping(value = BASE_URL, method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto dto, HttpServletResponse response) {
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(HttpHeaders.LOCATION, BASE_URL + "?userId=" + dto.getUserId());

        return ModelAndView.withJsonView()
            .addObject("user", user);
    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam String userId) {
        User user = findUserByUserId(userId);

        return ModelAndView.withJsonView()
            .addObject("user", user);
    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, @RequestBody UserUpdatedDto dto) {
        User user = findUserByUserId(userId);

        User updateUser = new User(user.getUserId(), user.getPassword(), dto.getName(), dto.getEmail());
        user.update(updateUser);

        return ModelAndView.withJsonView();
    }

    private User findUserByUserId(String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
}
