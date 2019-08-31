package next.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.util.HttpServletUtils;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private ObjectMapper om;

    public UserApiController() {
        om = new ObjectMapper();
        om.setVisibility(om.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        UserCreatedDto dto = HttpServletUtils.jsonBodyToObject(om, request, UserCreatedDto.class);

        User user = dto.toEntity();
        DataBase.addUser(user);
        User registeredUser = DataBase.findUserById(user.getUserId());
        if (!user.isSameUser(registeredUser)) {
            throw new IllegalArgumentException("사용자 등록 실패");
        }

        response.setStatus(201);
        response.setHeader("Location", "/api/users?userId=" + registeredUser.getUserId());
        return new ModelAndView(new JsonView(om));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        User user = findUser(getUserId(request));
        ModelAndView mav = new ModelAndView(new JsonView(om));

        if (user != null) {
            mav.addObject("user", user);
        }

        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) {
        String userId = getUserId(request);
        UserUpdatedDto dto = HttpServletUtils.jsonBodyToObject(om, request, UserUpdatedDto.class);

        User user = DataBase.findUserById(userId);
        user.update(dto.getName(), dto.getEmail());
        DataBase.updateById(user);

        logger.debug("user updated : {}", user.toString());
        return new ModelAndView(new JsonView(om));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        String userId = getUserId(request);
        User user = DataBase.deleteUser(userId);
        logger.debug("user deleted : {}", user.toString());

        return new ModelAndView(new JsonView(om));
    }

    private String getUserId(HttpServletRequest request) {
        return request.getParameter("userId");
    }

    private User findUser(String userId) {
        return DataBase.findUserById(userId);
    }
}
