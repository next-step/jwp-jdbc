package core.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

public class UserService {

    public static void addUser(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        DataBase.addUser(user);
    }

    public static User findUserById(String userId) {
        return DataBase.findUserById(userId);
    }

    public static void updateUser(String userId, UserUpdatedDto userUpdatedDto) {
        User user = new User(userId, null, userUpdatedDto.getName(), userUpdatedDto.getEmail());
        DataBase.updateUser(user);
    }
}
