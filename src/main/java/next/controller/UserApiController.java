package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static core.util.ObjectMapperUtils.objectMapper;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto userCreatedDto = objectMapper.readValue(request.getInputStream(), UserCreatedDto.class);
        User user = User.from(userCreatedDto);
        DataBase.addUser(user);
        ModelAndView mav = new ModelAndView(new JsonView());
        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(@RequestParam String userId) throws Exception {
        User user = DataBase.findUserById(userId);
        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = DataBase.findUserById(request.getParameter("userId"));
        UserUpdatedDto userUpdatedDto = objectMapper.readValue(request.getInputStream(), UserUpdatedDto.class);
        user.update(userUpdatedDto);
        DataBase.addUser(user);

        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }
}
