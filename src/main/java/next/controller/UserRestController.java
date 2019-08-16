package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
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

    UriBuilderFactory uriBuilder = new DefaultUriBuilderFactory();

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserCreatedDto dto = JsonUtils.readValue(req.getReader(), UserCreatedDto.class);
        User user = new User(
                dto.getUserId(),
                dto.getPassword(),
                dto.getName(),
                dto.getEmail());
        logger.debug("User : {}", user);
        DataBase.addUser(user);

        String location = uriBuilder.uriString("/api/users").queryParam("userId", dto.getUserId())
                .build().toString();
        resp.setStatus(HttpStatus.CREATED.value());
        resp.setHeader(HttpHeaders.LOCATION, location);
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findById(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", DataBase.findUserById(userId));
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserUpdatedDto dto = JsonUtils.readValue(req.getReader(), UserUpdatedDto.class);
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        user.update(dto);
        return new ModelAndView(new JsonView());
    }

}
