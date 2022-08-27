package next.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

@Controller
public class UserApiController {

    private static final Map<String, User> USERS = new ConcurrentHashMap<>();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        UserCreatedDto userCreatedDto = JsonUtils.toObject(request.getInputStream(), UserCreatedDto.class);

        var user = userCreatedDto.toUser();
        USERS.put(user.getUserId(), user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", "/api/users/" + user.getUserId());

        return ModelAndView.JsonView();
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable String id) {
        var user = USERS.get(id);

        var modelAndView = ModelAndView.JsonView();
        modelAndView.addObject("userId", user.getUserId());
        modelAndView.addObject("name", user.getName());
        modelAndView.addObject("email", user.getEmail());

        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws
        Exception {
        UserUpdatedDto userUpdatedDto = JsonUtils.toObject(request.getInputStream(), UserUpdatedDto.class);

        var user = USERS.get(id);
        user.modify(userUpdatedDto.getName(), userUpdatedDto.getEmail());

        return ModelAndView.JsonView();
    }
}
