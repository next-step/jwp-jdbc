package next.service;

import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public interface UserService {
	User create(UserCreatedDto userCreatedDto);

	User findUserById(String userId);

	void modify(String userId, UserUpdatedDto userUpdatedDto);
}
