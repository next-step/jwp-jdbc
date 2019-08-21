package next.dao;

import core.jdbc.JdbcTemplate;
import next.dao.mapper.UserListMapper;
import next.dao.mapper.UserMapper;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate.command(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        JdbcTemplate.command(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return JdbcTemplate.query("SELECT userId, password, name, email FROM USERS", UserListMapper.INSTANCE);
    }

    public User findByUserId(String userId) {
        return JdbcTemplate.query("SELECT userId, password, name, email FROM USERS WHERE userid=?", UserMapper.INSTANCE, userId);
    }
}
