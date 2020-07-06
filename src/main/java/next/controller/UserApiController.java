package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserCreatedDto userCreatedDto = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);

        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());

        DataBase.addUser(user);

        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUserById(@RequestParam String userId) throws Exception {
        User user = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = DataBase.findUserById(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        UserCreatedDto userCreatedDto = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);

        User userParameter = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        user.update(userParameter);

        DataBase.addUser(user);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

}
