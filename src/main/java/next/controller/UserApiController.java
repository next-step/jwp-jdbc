package next.controller;

import com.google.common.net.HttpHeaders;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(UserCreatedDto request, HttpServletResponse response) {
        User user = new User(
            request.getUserId(),
            request.getPassword(),
            request.getName(),
            request.getEmail()
        );

        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(HttpHeaders.LOCATION, "/api/users?userId=" + request.getUserId());

        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }
}
