package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.EntityNoFoundException;
import next.service.UserService;
import next.utils.IOUtils;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);


    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("Create User");
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        String content = IOUtils.readData(req.getReader(), req.getContentLength());
        UserCreatedDto createdDto = JsonUtils.toObject(content, UserCreatedDto.class);
        User user = UserService.create(createdDto);

        resp.setStatus(HttpStatus.CREATED.value());
        resp.addHeader("Location", String.format("/api/users?userId=%s", user.getUserId()));
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("Find Detail User");
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        String userId = req.getParameter("userId");
        User foundUser = UserService.findByUserId(userId);

        if (foundUser != null) {
            resp.setStatus(HttpStatus.OK.value());
            modelAndView.addObject("user", foundUser);
        } else {
            resp.setStatus(HttpStatus.NO_CONTENT.value());
        }

        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("Update User");
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        String userId = req.getParameter("userId");
        String content = IOUtils.readData(req.getReader(), req.getContentLength());
        UserUpdatedDto updatedDto = JsonUtils.toObject(content, UserUpdatedDto.class);

        try {
            UserService.update(userId, updatedDto);
            resp.setStatus(HttpStatus.OK.value());
        } catch (EntityNoFoundException e) {
            logger.error("User not found: ", e);
            resp.setStatus(HttpStatus.NOT_FOUND.value());
        }

        return modelAndView;
    }
}
