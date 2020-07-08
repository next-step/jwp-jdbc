package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.DuplicateUserIdException;
import next.exception.SqlErrorCode;
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
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    public User insertUser(User user) {
        try {
            dao.insert(user);
            return user;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            if (ex.getErrorCode() == SqlErrorCode.ERR_DUP_ENTRY.getCode()) {
                throw new DuplicateUserIdException(ex);
            }
            throw new RuntimeException("user insert fail");
        }
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
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException("user update fail");
        }
    }

    public User updateUser(String userId, UserUpdatedDto userUpdatedDto) {
        User user = new User(userId, null, userUpdatedDto.getName(), userUpdatedDto.getEmail());
        return updateUser(user);
    }
}
