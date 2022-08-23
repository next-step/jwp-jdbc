package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserService {
	private final UserDao userDao = new UserDao();

	public User create(UserCreatedDto userCreatedDto) {
		User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
		userDao.insert(user);
		return user;
	}

	public User findUserById(String userId) {
		return userDao.findByUserId(userId);
	}

	public void modify(String userId, UserUpdatedDto userUpdatedDto) {
		User findUser = userDao.findByUserId(userId);
		userDao.update(new User(findUser.getUserId(), findUser.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));
	}
}
