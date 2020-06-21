package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignUpController {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView signUp(UserCreatedDto userCreatedDto, HttpServletResponse response) throws Exception {
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
    public ModelAndView create(String userId) throws Exception {
        return redirect("/");
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(JspView.DEFAULT_REDIRECT_PREFIX + path));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ModelAndView mav = new ModelAndView(new JspView("/user/list.jsp"));
        mav.addObject("users", DataBase.findAll());
        return mav;
    }

}
