package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView createUser(UserCreatedDto userCreatedDto) {
        logger.info(userCreatedDto.getEmail());
        return new ModelAndView(new JsonView());
    }

}
