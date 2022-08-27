package core.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import java.sql.SQLException;

public class UserService {

    public static void addUser(UserCreatedDto userCreatedDto) throws SQLException {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(), userCreatedDto.getName(), userCreatedDto.getEmail());
        UserDao userDao = new UserDao();
        userDao.insert(user);
    }

    public static User findUserById(String userId) throws SQLException {
        UserDao userDao = new UserDao();
        return userDao.findByUserId(userId);
    }

    public static void updateUser(String userId, UserUpdatedDto userUpdatedDto) throws SQLException {
        User user = new User(userId, null, userUpdatedDto.getName(), userUpdatedDto.getEmail());
        UserDao userDao = new UserDao();
        userDao.update(user);
    }
}
