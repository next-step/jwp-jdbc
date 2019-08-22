package next.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserService {
    public User create(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);
        return user;
    }

    public User findByUserId(String userId) {
        return DataBase.findUserById(userId);
    }

    public void update(String userId, UserUpdatedDto userUpdatedDto) {
        User user = DataBase.findUserById(userId);
        user.update(userUpdatedDto);
    }
}
