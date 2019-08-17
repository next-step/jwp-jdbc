package next.service;

import core.db.DataBase;
import next.converter.UserConverter;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserService {

    public void createUser(UserCreatedDto dto) {
        User user = UserConverter.convertToUserBy(dto);
        DataBase.addUser(user);
    }

    public User findBy(String userId) {
        return DataBase.findUserById(userId);
    }

    public void update(String userId, UserUpdatedDto dto) {
        User user = findBy(userId);
        User updatedUser = UserConverter.convertToUserBy(dto);
        user.update(updatedUser);
    }
}
