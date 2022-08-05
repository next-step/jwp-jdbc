package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.exception.UserNotFoundException;
import next.model.User;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class UserApiController {

    private static final String BASE_URL = "/api/users";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = BASE_URL, method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        UserCreatedDto dto = objectMapper.readValue(requestBody, UserCreatedDto.class);
        User user = dto.toUser();
        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(HttpHeaders.LOCATION, BASE_URL + "/api/users?userId=" + dto.getUserId());

        ModelAndView mav = ModelAndView.withJsonView();
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.GET)
    public ModelAndView findUser(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        User user = DataBase.findUserById(userId);

        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        ModelAndView mav = ModelAndView.withJsonView();
        mav.addObject("user", user);
        return mav;
    }
}
