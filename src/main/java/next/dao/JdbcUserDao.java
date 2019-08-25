package next.dao;

import core.jdbc.JdbcTemplate;
import next.dao.mapper.UserMapper;
import next.model.User;

import java.util.List;

public enum JdbcUserDao implements UserDao {
    INSTANCE;

    @Override
    public void insert(User user) {
        JdbcTemplate.command(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    @Override
    public void update(User user) {
        JdbcTemplate.command(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    @Override
    public List<User> findAll() {
        return JdbcTemplate.queryForList("SELECT userId, password, name, email FROM USERS", UserMapper.INSTANCE);
    }

    @Override
    public User findByUserId(String userId) {
        return JdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?", UserMapper.INSTANCE, userId);
    }
}
