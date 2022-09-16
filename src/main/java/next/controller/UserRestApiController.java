package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserRestApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto dto, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView(new JsonView());
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        DataBase.addUser(user);
        mv.addObject("user", dto);

        response.setStatus(201);
        response.setHeader("location", "/api/users/" + user.getUserId());

        return mv;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.GET)
    public ModelAndView read(@PathVariable String userId) {
        ModelAndView mv = new ModelAndView(new JsonView());

        User user = DataBase.findUserById(userId);
        mv.addObject("user", user);

        return mv;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PUT)
    public ModelAndView update(@PathVariable String userId, @RequestBody UserUpdatedDto updatedDto) {
        ModelAndView mv = new ModelAndView(new JsonView());

        User user = DataBase.findUserById(userId);
        user.update(new User(userId, user.getPassword(), updatedDto.getName(), updatedDto.getEmail()));

        return mv;
    }
}
