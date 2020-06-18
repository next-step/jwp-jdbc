package next.controller;

import core.annotation.RequestBody;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.view.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author KingCjy
 */
@Controller
@RequestMapping("/api/users")
public class UserApiController {

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletResponse response, @RequestBody UserCreatedDto userCreatedDto) {
        DataBase.addUser(userCreatedDto.toUser());

        ModelAndView modelAndView = new ModelAndView(new JsonView());

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader(HttpHeaders.LOCATION, " /api/users?userId=" + userCreatedDto.getUserId());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUser(String userId) {
        User user = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView updateUser(String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);

        if(user == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return new ModelAndView(new JsonView());
        }

        user.update(userUpdatedDto);

        return new ModelAndView(new JsonView());
    }
}
