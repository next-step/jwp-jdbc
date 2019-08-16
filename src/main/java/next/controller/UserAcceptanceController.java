package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.common.CommonUtil;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class UserAcceptanceController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView userQuery(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("User : {}", req.getParameter("userId"));

        User user = DataBase.findUserById(req.getParameter("userId"));
        return new ModelAndView(new JsonView())
                .addObject("user", user);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ModelAndView userCreate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> paramMap = CommonUtil.getParameter(request);

        DataBase.addUser(new User(paramMap.get("userId").toString(),
                paramMap.get("password").toString(),
                paramMap.get("name").toString(),
                paramMap.get("email").toString()));

        response.setHeader("location", "/api/users?userId=" + paramMap.get("userId"));
        response.setStatus(HttpStatus.CREATED.value());
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    public ModelAndView userModify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> paramMap = CommonUtil.getParameter(request);
        User updateUser = new User(
                paramMap.get("name").toString(),
                paramMap.get("email").toString());

        DataBase.modifyUser(request.getParameter("userId"), updateUser);

        response.setHeader("location", "/api/users?userId=" + request.getParameter("userId"));
        return new ModelAndView(new JsonView());
    }




}
