package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.util.QueryStringUtil;
import core.util.RequestBodyReader;
import lombok.extern.slf4j.Slf4j;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static core.web.ResponseHeaderKeys.LOCATION;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;

@Slf4j
@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto userCreatedDto = RequestBodyReader.read(request, UserCreatedDto.class);
        log.debug("create user : userCreatedDto={}", userCreatedDto);

        User user = userCreatedDto.toEntity();
        DataBase.addUser(user);

        String location = QueryStringUtil.generateQueryStringWithUri("/api/users", Collections.singletonMap("userId", user.getUserId()));
        response.setHeader(LOCATION, location);
        response.setStatus(SC_CREATED);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam String userId) {
        log.debug("get user : userId={}", userId);
        User user = DataBase.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam String userId, HttpServletRequest request) throws IOException {
        log.debug("update user : userId={}", userId);
        User user = DataBase.findUserById(userId);

        UserUpdatedDto userUpdatedDto = RequestBodyReader.read(request, UserUpdatedDto.class);
        user.update(userUpdatedDto);

        return new ModelAndView(new JsonView());
    }
}
