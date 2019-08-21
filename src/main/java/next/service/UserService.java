package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;

import java.sql.SQLException;
import java.util.Objects;

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

    public void updateUser(String userId, UserUpdatedDto updatedDto) {
        try {
            User user = userDao.findByUserId(userId);

            if (Objects.isNull(user)) {
                throw new IllegalArgumentException("userId 가 없습니다. userId : ["+ userId + "]");
            }
            userDao.update(new User(user.getUserId(), user.getPassword(), updatedDto.getName(), updatedDto.getEmail()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
