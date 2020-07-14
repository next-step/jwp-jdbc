package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.DataAccessException;
import next.exception.DuplicateUserIdException;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserDao dao = new UserDao();

    public User findUserById(String userId) {
        return dao.findByUserId(userId);
    }

    public User insertUser(User user) {
        try {
            dao.insert(user);
            return user;
        } catch (DataAccessException ex) {
            if (ex.isDuplicated()) {
                throw new DuplicateUserIdException(ex);
            }
            return null;
        }
    }

    public User insertUser(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return insertUser(user);
    }

    public User updateUser(User updateUser) {
        dao.update(updateUser);
        return dao.findByUserId(updateUser.getUserId());
    }

    public User updateUser(String userId, UserUpdatedDto userUpdatedDto) {
        User user = new User(userId, null, userUpdatedDto.getName(), userUpdatedDto.getEmail());
        return updateUser(user);
    }
}
