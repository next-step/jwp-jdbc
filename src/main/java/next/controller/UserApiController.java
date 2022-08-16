package next.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import core.annotation.web.Controller;
import core.annotation.web.RequestBody;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.annotation.web.RequestParam;
import core.db.DataBase;
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.model.User;

@Controller
public class UserApiController {
	private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

	@RequestMapping(value = "/api/users", method = RequestMethod.POST)
	public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
		logger.debug("UserCreatedDto : {}", userCreatedDto);
		User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
		DataBase.addUser(user);
		response.setStatus(HttpStatus.CREATED.value());
		response.setHeader("Location", "/api/users?userId=" + user.getUserId());
		return new ModelAndView(new JsonView());
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public ModelAndView findOne(@RequestParam String userId) {
		logger.debug("userId: {}", userId);
		User user = DataBase.findUserById(userId);
		ModelAndView mav = new ModelAndView(new JsonView());
		mav.addObject("user", user);
		return mav;
	}
}
