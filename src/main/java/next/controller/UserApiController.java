package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto request, HttpServletResponse response) {
        logger.debug("request : {}", request);
        DataBase.addUser(request.toUser());

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", "/api/users?userId=" + request.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView read(@RequestParam("userId") String userId) {
        logger.debug("userId : {}", userId);
        User user = DataBase.getUserById(userId);

        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView update(@RequestParam("userId") String userId, @RequestBody UserUpdatedDto request) {
        logger.debug("userId : {}", userId);
        logger.debug("request : {}", request);

        User user = DataBase.getUserById(userId);
        user.update(request.getName(), request.getEmail());
        DataBase.addUser(user);

        return new ModelAndView(new JsonView());
    }
}
