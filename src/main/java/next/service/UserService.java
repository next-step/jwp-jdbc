package next.service;

import core.jdbc.JdbcContext;
import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.dto.UserUpdatedDto;
import next.exception.NotFoundException;
import next.model.User;

public class UserService {

    private static final  UserDao userDao = new UserDao(new JdbcContext());

    public User create(final UserCreatedDto userCreatedDto) {
        userDao.insert(userCreatedDto.toUser());
        return userDao.findByUserId(userCreatedDto.getUserId())
                .orElseThrow(NotFoundException::new);
    }

    public User findByUserId(final String userId) {
        return userDao.findByUserId(userId)
                .orElseThrow(NotFoundException::new);
    }

    public User update(final String userId, final UserUpdatedDto userUpdatedDto) {
        User user = findByUserId(userId);
        user.update(userUpdatedDto.toUser(user.getUserId(), user.getPassword()));
        userDao.update(user);
        return user;
    }
}
