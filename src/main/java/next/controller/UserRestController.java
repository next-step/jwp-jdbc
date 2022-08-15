package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserRestController {

    private static final String BASE_PATH = "/api/users";

    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto createdDto, HttpServletResponse response) {
        User user = new User(createdDto.getUserId(), createdDto.getPassword(), createdDto.getName(), createdDto.getEmail());
        DataBase.addUser(user);
        response.setStatus(HttpStatus.CREATED.value());
        response.addHeader(HttpHeaders.LOCATION, String.format("%s/%s", BASE_PATH, user.getUserId()));
        return new ModelAndView(new JsonView())
                .addObject("user", UserDto.from(user));
    }

    @RequestMapping(value = BASE_PATH + "/{id}", method = RequestMethod.GET)
    public ModelAndView retrieve(@PathVariable String id, HttpServletResponse response) {
        User user = DataBase.findUserById(id);
        validateUserExists(user);
        response.setStatus(HttpStatus.OK.value());
        return new ModelAndView(new JsonView())
                .addObject("user", UserDto.from(user));
    }

    @RequestMapping(value = BASE_PATH + "/{id}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable String id, @RequestBody UserUpdatedDto updatedDto, HttpServletResponse response) {
        User user = DataBase.findUserById(id);
        validateUserExists(user);
        user.update(new User(user.getUserId(), user.getPassword(), updatedDto.getName(), updatedDto.getEmail()));
        response.setStatus(HttpStatus.OK.value());
        return new ModelAndView(new JsonView());
    }

    private void validateUserExists(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }
}
