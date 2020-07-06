package next.service;

import core.db.DataBase;
import next.dto.UserCreatedDto;
import next.model.User;

public class UserService {

    public static User findUserById(String userId) {
        return DataBase.findUserById(userId);
    }

    public static User insertUser(User user) {
        DataBase.addUser(user);
        return user;
    }

    public static User insertUser(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return insertUser(user);
    }

    public static User updateUser(User updateUser) {
        User dbUser = findUserById(updateUser.getUserId());
        dbUser.update(updateUser);

        DataBase.addUser(dbUser);
        return dbUser;
    }

    public static User updateUser(String userId, UserCreatedDto userCreatedDto) {
        User user = new User(userId, userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return updateUser(user);
    }
}
