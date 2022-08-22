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
import core.mvc.JsonView;
import core.mvc.ModelAndView;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import next.service.UserService;
import next.service.UserServiceImpl;

@Controller
public class UserApiController {
	private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);
	private final UserService userService = new UserServiceImpl();

	@RequestMapping(value = "/api/users", method = RequestMethod.POST)
	public ModelAndView create(@RequestBody UserCreatedDto userCreatedDto, HttpServletResponse response) {
		logger.debug("UserCreatedDto : {}", userCreatedDto);

		User user = userService.create(userCreatedDto);

		response.setStatus(HttpStatus.CREATED.value());
		response.setHeader("Location", "/api/users?userId=" + user.getUserId());
		return new ModelAndView(new JsonView());
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public ModelAndView findOne(@RequestParam String userId, HttpServletResponse response) {
		logger.debug("userId: {}", userId);

		User user = userService.findUserById(userId);

		ModelAndView mav = new ModelAndView(new JsonView());
		mav.addObject("user", user);
		response.setStatus(HttpStatus.OK.value());
		return mav;
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.PUT)
	public ModelAndView modify(@RequestParam String userId, @RequestBody UserUpdatedDto userUpdatedDto, HttpServletResponse response) {
		logger.debug("userId: {}, UserUpdatedDto: {}", userId, userUpdatedDto);

		userService.modify(userId, userUpdatedDto);

		response.setStatus(HttpStatus.OK.value());
		return new ModelAndView(new JsonView());
	}
}
