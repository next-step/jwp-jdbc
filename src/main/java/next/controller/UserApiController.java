package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;

import next.dto.UserCreatedDto;
import next.model.User;
import org.springframework.http.HttpStatus;

/**
 * Created By kjs4395 on 6/30/20
 */
@Controller
public class UserApiController {

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(UserCreatedDto userCreatedDto) {
        DataBase.addUser(User.userCreateDtoToUser(userCreatedDto));

        return new ModelAndView(new JsonView(HttpStatus.CREATED, "/api/users?userId="+userCreatedDto.getUserId()));
    }

}
