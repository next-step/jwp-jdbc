package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.di.factory.SingletonFactory;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dao.NewUserDao;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private UriBuilderFactory uriBuilder = new DefaultUriBuilderFactory();

    private UserDao userDao = SingletonFactory.getBean(NewUserDao.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView insert(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserCreatedDto dto = JsonUtils.readValue(req.getReader(), UserCreatedDto.class);
        User user = new User(
                dto.getUserId(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail());
        logger.debug("User : {}", user);

        userDao.insert(user);

        String location = uriBuilder.uriString("/api/users").queryParam("userId", dto.getUserId())
                .build().toString();
        resp.setStatus(HttpStatus.CREATED.value());
        resp.setHeader(HttpHeaders.LOCATION, location);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findAll(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", userDao.findAll());
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserUpdatedDto dto = JsonUtils.readValue(req.getReader(), UserUpdatedDto.class);
        String userId = req.getParameter("userId");
        User user = userDao.findByUserId(userId);
        user.update(dto);
        return new ModelAndView(new JsonView());
    }

}
