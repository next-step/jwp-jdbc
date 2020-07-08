package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final int RETRY_MAX_COUNT = 3;
    private static final int RETRY_SLEEP_TIME = 1 * 1000;

    private UserDao dao = new UserDao();

    public User findUserById(String userId) throws InterruptedException {

        int maxRetry = RETRY_MAX_COUNT;
        while (maxRetry-- > 0) {
            try {
                return dao.findByUserId(userId);
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.sleep(RETRY_SLEEP_TIME);
            }
        }
        throw new RuntimeException("DB 조회 실패");
    }

    public User insertUser(User user) throws InterruptedException {
        int maxRetry = RETRY_MAX_COUNT;
        while (maxRetry-- > 0) {
            try {
                dao.insert(user);
                return user;
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.sleep(RETRY_SLEEP_TIME);
            }
        }
        throw new RuntimeException("DB 저장 실패");


    }

    public User insertUser(UserCreatedDto userCreatedDto) throws InterruptedException {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword(),
                userCreatedDto.getName(), userCreatedDto.getEmail());
        return insertUser(user);
    }

    public User updateUser(User updateUser) throws InterruptedException {
        int maxRetry = RETRY_MAX_COUNT;
        while (maxRetry-- > 0) {
            try {
                dao.update(updateUser);
                return dao.findByUserId(updateUser.getUserId());
            } catch (SQLException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.sleep(RETRY_SLEEP_TIME);
            }
        }
        throw new RuntimeException("DB 저장 실패");
    }

    public User updateUser(String userId, UserUpdatedDto userUpdatedDto) throws InterruptedException {
        User user = new User(userId, null, userUpdatedDto.getName(), userUpdatedDto.getEmail());
        return updateUser(user);
    }
}
