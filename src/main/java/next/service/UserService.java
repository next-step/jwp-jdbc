package next.service;

import next.dao.UserDao;
import next.dto.UserCreatedDto;
import next.model.User;

import java.sql.SQLException;

public class UserService {

    private static final  UserDao userDao = new UserDao();

    public User create(UserCreatedDto userCreatedDto) throws SQLException {
        userDao.insert(userCreatedDto.toUser());
        return userDao.findByUserId(userCreatedDto.getUserId());
    }
}
