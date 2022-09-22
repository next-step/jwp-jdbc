package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ApiUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApiUserController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(UserCreatedDto userCreatedDto) {
        return new ModelAndView();
    }
}
