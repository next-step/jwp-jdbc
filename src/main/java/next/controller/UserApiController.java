package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.util.QueryStringUtil;
import lombok.extern.slf4j.Slf4j;
import next.dto.UserCreatedDto;
import next.model.User;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static core.web.ResponseHeaderKeys.LOCATION;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto userCreatedDto = readUserCreatedDtoFrom(request);
        log.debug("create user : userCreatedDto={}", userCreatedDto);

        User user = userCreatedDto.toEntity();
        DataBase.addUser(user);

        String location = QueryStringUtil.generateQueryStringWithUri("/api/users", Collections.singletonMap("userId", user.getUserId()));
        response.setHeader(LOCATION, location);
        response.setStatus(SC_CREATED);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView getUser(@RequestParam String userId, HttpServletResponse response) {
        log.debug("get user : userId={}", userId);
        User user = DataBase.findUserById(userId);

        response.setStatus(SC_OK);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    private UserCreatedDto readUserCreatedDtoFrom(HttpServletRequest request) throws IOException {
        String body = IOUtils.toString(request.getReader());

        return JsonUtils.toObject(body, UserCreatedDto.class);
    }
}
