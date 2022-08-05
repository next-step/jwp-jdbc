package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.UserNotFoundException;
import next.model.User;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
public class UserApiController {

    private static final String BASE_URL = "/api/users";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = BASE_URL, method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreatedDto dto = convertRequestBodyToObject(request, UserCreatedDto.class);
        User user = dto.toUser();
        DataBase.addUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(HttpHeaders.LOCATION, BASE_URL + "?userId=" + dto.getUserId());

        return ModelAndView.withJsonView()
            .addObject("user", user);
    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.GET)
    public ModelAndView findUser(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        User user = findUser(userId);

        return ModelAndView.withJsonView()
            .addObject("user", user);
    }

    @RequestMapping(value = BASE_URL, method = RequestMethod.PUT)
    public ModelAndView updateUser(HttpServletRequest request) throws IOException {
        String userId = request.getParameter("userId");
        User user = findUser(userId);

        UserUpdatedDto dto = convertRequestBodyToObject(request, UserUpdatedDto.class);
        User updateUser = new User(user.getUserId(), user.getPassword(), dto.getName(), dto.getEmail());

        user.update(updateUser);

        return ModelAndView.withJsonView();
    }

    private <T> T convertRequestBodyToObject(HttpServletRequest request, Class<T> clazz) throws IOException {
        String requestBody = StreamUtils.copyToString(request.getInputStream(), UTF_8);
        return objectMapper.readValue(requestBody, clazz);
    }

    private User findUser(String userId) {
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
}
