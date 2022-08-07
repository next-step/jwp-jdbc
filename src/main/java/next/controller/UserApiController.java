package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.PathVariable;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView create(@RequestBody UserCreatedDto dto, HttpServletResponse response) throws Exception {
        ModelAndView mv = createModelAndView();
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        logger.debug("User : {}", dto);
        DataBase.addUser(user);
        mv.addObject("user", dto);

        response.setStatus(HttpStatus.CREATED.value());
        response.addHeader("Location", "/api/users/" + dto.getUserId());
        return mv;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.GET)
    public ModelAndView readUser(@PathVariable String userId) {
        ModelAndView mv = createModelAndView();
        logger.debug("userId : {}", userId);

        User foundUser = DataBase.findUserById(userId);
        mv.addObject("user", foundUser);

        return mv;
    }

    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.PUT)
    public ModelAndView updateUser(@PathVariable String userId,@RequestBody UserUpdatedDto updatedDto) {
        ModelAndView mv = createModelAndView();
        logger.debug("userUpdateDto : {}", updatedDto);

        User foundUser = DataBase.findUserById(userId);
        foundUser.update(new User(userId, foundUser.getPassword(), updatedDto.getName(), updatedDto.getEmail()));

        return mv;
    }


    private ModelAndView createModelAndView() {
        return new ModelAndView(new JsonView());
    }


}
