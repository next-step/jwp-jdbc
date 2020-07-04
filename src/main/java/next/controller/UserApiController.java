package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.util.QueryStringUtil;
import next.dto.UserCreatedDto;
import next.model.User;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static core.web.ResponseHeaderKeys.LOCATION;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto userCreatedDto = readUserCreatedDtoFrom(request);
        User user = userCreatedDto.toEntity();
        DataBase.addUser(user);

        String location = QueryStringUtil.generateQueryStringWithUri("/api/users", Collections.singletonMap("userId", user.getUserId()));
        response.setHeader(LOCATION, location);
        response.setStatus(SC_CREATED);
        return new ModelAndView(new JsonView());
    }

    private UserCreatedDto readUserCreatedDtoFrom(HttpServletRequest request) throws IOException {
        String body = IOUtils.toString(request.getReader());

        return JsonUtils.toObject(body, UserCreatedDto.class);
    }
}
