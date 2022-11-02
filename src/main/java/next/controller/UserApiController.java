package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) throws Exception {
        User user = User.from(userCreatedDto);

        DataBase.addUser(user);

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", "/api/users?userId=" + userCreatedDto.getUserId());

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView findUser(@RequestParam(value = "userId") String userId, HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        if(user == null) {
            System.out.println("user 를 찾지 못함");
        }

        response.setStatus(HttpStatus.OK.value());

        ModelAndView mv = new ModelAndView(new JsonView());
        mv.addObject("user", user);
        return mv;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView updateUser(@RequestParam(value = "userId") String userId,
                                   @RequestBody UserUpdatedDto userUpdatedDto,
                                   HttpServletResponse response) {
        User user = DataBase.findUserById(userId);
        user.update(new User(user.getUserId(), user.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));

        response.setStatus(HttpStatus.OK.value());

        DataBase.addUser(user);

        ModelAndView mv = new ModelAndView(new JsonView());
        mv.addObject("user", user);
        return mv;
    }
}
