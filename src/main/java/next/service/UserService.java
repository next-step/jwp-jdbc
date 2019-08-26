package next.service;

import next.dao.JdbcContext;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.NotFoundException;
import next.model.User;

import java.sql.SQLException;

public class UserService {

    private static final  UserDao userDao = new UserDao(new JdbcContext());

    public User create(final UserCreatedDto userCreatedDto) throws SQLException {
        userDao.insert(userCreatedDto.toUser());
        return userDao.findByUserId(userCreatedDto.getUserId())
                .orElseThrow(NotFoundException::new);
    }

    public User findByUserId(final String userId) throws SQLException {
        return userDao.findByUserId(userId)
                .orElseThrow(NotFoundException::new);
    }

    public User update(final String userId, final UserUpdatedDto userUpdatedDto) throws SQLException {
        User user = findByUserId(userId);
        user.update(userUpdatedDto.toUser(user.getUserId(), user.getPassword()));
        userDao.update(user);
        return user;
    }
}
