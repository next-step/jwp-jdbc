package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.BeanRegistry;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    private static final String PARAMETER_USER_ID = "userId";
    private static final String ATTR_NAME_USER = "user";

    private final UserDao userDao;

    public ApiUserController() {
        this.userDao = BeanRegistry.getBean(UserDao.class);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserCreatedDto userCreatedDto = JsonUtils.toObject(req.getInputStream(), UserCreatedDto.class);

        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        userDao.insert(user);

        resp.setHeader(HttpHeaders.LOCATION, req.getRequestURI() + "?" + PARAMETER_USER_ID + "=" + user.getUserId());
        resp.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter(PARAMETER_USER_ID);

        User user = userDao.findByUserId(userId);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        logger.debug(">>>>{}", user);
        modelAndView.addObject(ATTR_NAME_USER, user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter(PARAMETER_USER_ID);
        UserUpdatedDto userUpdatedDto = JsonUtils.toObject(req.getInputStream(), UserUpdatedDto.class);

        User user = userDao.findByUserId(userId);
        user.update(userUpdatedDto);
        userDao.update(user);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        return modelAndView;
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(
                JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

}
