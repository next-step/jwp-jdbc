package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import java.sql.SQLException;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public void createUser(UserCreatedDto createdDto) {
        User user = new User(createdDto.getUserId(), createdDto.getPassword(), createdDto.getName(), createdDto.getEmail());
        try {
            userDao.insert(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String userId) {
        try {
            return userDao.findByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(UserUpdatedDto updatedDto) {

    }
}
