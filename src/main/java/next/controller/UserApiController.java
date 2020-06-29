package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.annotation.web.ResponseBody;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static org.springframework.http.HttpHeaders.LOCATION;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@ResponseBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws Exception {
        response.setStatus(SC_CREATED);
        response.setHeader(LOCATION, String.format("/api/users?userId=%s", userCreatedDto.getUserId()));
        DataBase.addUser(userCreatedDto.toEntity());
        return new ModelAndView(new JsonView());
    }


    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam(value = "userId") String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam(value = "userId") String userId, @ResponseBody UserUpdatedDto userUpdatedDto) throws Exception {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.getName(), userUpdatedDto.getEmail());
        return new ModelAndView(new JsonView());
    }

}
