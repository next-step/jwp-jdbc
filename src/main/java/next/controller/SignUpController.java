package next.controller;

import core.annotation.web.*;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserFindDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Controller
public class SignUpController {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView signUp(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws Exception {
        log.debug("UserCreatedDto: {}", userCreatedDto);
        final User user = new User(
                userCreatedDto.getUserId(),
                userCreatedDto.getPassword(),
                userCreatedDto.getName(),
                userCreatedDto.getEmail());
        DataBase.addUser(user);
        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", "/api/users?userId=" + user.getUserId());
        final ModelAndView mav = new ModelAndView(new JsonView());
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam String userId) throws Exception {
        log.debug("userId: {}", userId);
        final User user = DataBase.findUserById(userId);
        final UserFindDto dto = new UserFindDto(user);
        final ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("foundUser", dto);
        return mav;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView modifyUser(@RequestParam String userId, @RequestBody UserUpdatedDto dto) throws Exception {
        log.debug("userId: {}, dto: {}", userId, dto);
        final User foundUser = DataBase.findUserById(userId);
        foundUser.update(new User("", "", dto.getName(), dto.getEmail()));
        DataBase.addUser(foundUser);
        ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("updatedUser", new UserFindDto(foundUser));
        return mav;
    }

}
