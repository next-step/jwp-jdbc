package next.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;
import next.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private UserService service = new UserService();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserCreatedDto userCreatedDto = convertStreamToUserCreatedDto(request.getInputStream());

        User user = service.insertUser(userCreatedDto);

        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUserById(@RequestParam String userId) throws Exception {
        User user = service.findUserById(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserCreatedDto userCreatedDto = convertStreamToUserCreatedDto(request.getInputStream());

        User user = service.updateUser(userId, userCreatedDto);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    private UserCreatedDto convertStreamToUserCreatedDto(InputStream inputStream) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, UserCreatedDto.class);
    }
}
