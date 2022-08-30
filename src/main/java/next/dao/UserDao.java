package next.dao;

import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(
                "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail()
        );
    }

    public void update(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(
                "UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                user.getName(), user.getEmail(), user.getUserId()
        );
    }

    public List<User> findAll() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.selectList(
                "SELECT userId, password, name, email FROM USERS",
                User.class
        );
    }

    public User findByUserId(String userId) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.selectObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                User.class, userId
        );
    }
}
