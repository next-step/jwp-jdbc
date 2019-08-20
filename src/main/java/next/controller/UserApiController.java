package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonUtils;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserApiController {

    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);
    private final String BASE_PATH = "/api/users";

    @RequestMapping(value = BASE_PATH, method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest request,
                             final HttpServletResponse response) throws Exception {
        final UserCreatedDto userCreatedDto = JsonUtils.toObject(request.getInputStream(), UserCreatedDto.class);
        DataBase.addUser(userCreatedDto.toEntity());

        final String userId = userCreatedDto.getUserId();

        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader("Location", BASE_PATH + "?userId=" +userId);

        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = BASE_PATH, method = RequestMethod.GET)
    public ModelAndView findByUserId(final HttpServletRequest request,
                                     final HttpServletResponse response) {
        final String userId = request.getParameter("userId");
        final User user = DataBase.findUserById(userId);

        final ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);

        return mav;
    }

    @RequestMapping(value = BASE_PATH, method = RequestMethod.PUT)
    public ModelAndView updateByUserId(final HttpServletRequest request,
                                       final HttpServletResponse response) throws Exception {
        final UserUpdatedDto userUpdatedDto = JsonUtils.toObject(request.getInputStream(), UserUpdatedDto.class);

        final String userId = request.getParameter("userId");
        final User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto.toEntity());

        final ModelAndView mav = new ModelAndView(new JsonView());
        mav.addObject("user", user);

        return mav;
    }
}
