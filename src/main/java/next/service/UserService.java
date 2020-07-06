package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDao dao = new UserDao();

    public User findUserById(String userId) {
        try {
            return dao.findByUserId(userId);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public User insertUser(User user) {
        try {
            dao.insert(user);
            return user;
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public User insertUser(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return insertUser(user);
    }

    public User updateUser(User updateUser) {
        try {
            dao.update(updateUser);
            return dao.findByUserId(updateUser.getUserId());
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public User updateUser(String userId, UserCreatedDto userCreatedDto) {
        User user = new User(userId, userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return updateUser(user);
    }
}
