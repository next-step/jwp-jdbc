package next.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserServiceImpl implements UserService {
	@Override
	public User create(UserCreatedDto userCreatedDto) {
		User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
		DataBase.addUser(user);
		return user;
	}

	@Override
	public User findUserById(String userId) {
		return DataBase.findUserById(userId);
	}

	@Override
	public void modify(String userId, UserUpdatedDto userUpdatedDto) {
		User findUser = DataBase.findUserById(userId);
		DataBase.addUser(new User(findUser.getUserId(), findUser.getPassword(), userUpdatedDto.getName(), userUpdatedDto.getEmail()));
	}
}
