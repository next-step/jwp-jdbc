package next.controller;

import core.annotation.RequestBody;
import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;

/**
 * @author KingCjy
 */
@Controller
@RequestMapping("/api")
public class UserApiController {

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView createUser(@RequestBody UserCreatedDto userCreatedDto) {
        return new ModelAndView();
    }
}
