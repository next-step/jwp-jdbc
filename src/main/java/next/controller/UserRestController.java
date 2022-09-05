package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws Exception {
        logger.debug("User : {}", userCreatedDto.getUserId());
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);

        response.setStatus(201);
        response.setHeader("location", "/api/users?userId=" + user.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView selectUser(@RequestParam(value = "userId") String userId, HttpServletResponse response) throws Exception {
        logger.debug("userId : {}", userId);
        User userById = DataBase.findUserById(userId);

        response.setStatus(200);
        return new ModelAndView(new JsonView()).addObject("user", userById);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam(value = "userId") String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) throws Exception {
        logger.debug("UserId : {}, UserUpdatedDto : {}", userId, userUpdatedDto);
        User userById = DataBase.findUserById(userId);

        User updateUser = new User(userById.getUserId(), userById.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail());

        userById.update(updateUser);

        response.setStatus(200);
        return new ModelAndView(new JsonView()).addObject("user", userById);
    }
}
