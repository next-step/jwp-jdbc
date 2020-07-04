package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import core.mvc.View;
import javax.servlet.http.HttpServletResponse;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Controller
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody User user, HttpServletResponse response)
        throws Exception {
        logger.debug("User : {}", user);
        DataBase.addUser(user);

        response.setHeader(HttpHeaders.LOCATION, "/api/users/" + user.getUserId());
        response.setStatus(HttpStatus.CREATED.value());

        View view = new JsonView();
        ModelAndView modelAndView = new ModelAndView(view);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable String userId) {
        View view = new JsonView();
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("user", DataBase.findUserById(userId));
        return modelAndView;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PUT)
    public ModelAndView modify(@PathVariable String userId, @RequestBody User user) {
        User targetUser = DataBase.findUserById(userId);
        targetUser.update(user);
        DataBase.addUser(targetUser);

        View view = new JsonView();
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("user", targetUser);
        return modelAndView;
    }
}
