package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.LOCATION;

@Controller
public class UserAcceptanceController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
        DataBase.addUser(new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail()));

        response.setHeader(LOCATION, "/api/users?userId=" + userCreatedDto.getUserId());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView read(HttpServletRequest request) {
        User user = findUser(request.getParameter("userId"));

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response, @RequestBody UserUpdatedDto userUpdatedDto) {
        User user = findUser(request.getParameter("userId"));
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());

        response.setStatus(HttpServletResponse.SC_OK);
        return new ModelAndView(new JsonView());
    }


    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(
                JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

    private User findUser(String userId) {
        User user = DataBase.findUserById(userId);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("유저를 찾을수 없습니다.");
        }
        return user;
    }
}
