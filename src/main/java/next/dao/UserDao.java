package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.ObjectRowMapper;
import next.model.User;

import java.util.List;

public class UserDao {
    private static final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    public void insert_varargs(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void insert_ps(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", ps -> {
                    ps.setString(1, user.getUserId());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getName());
                    ps.setString(4, user.getEmail());
                }
        );
    }

    public void update_varargs(User user) {
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public void update_ps(User user) {
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", ps -> {
                    ps.setString(1, user.getPassword());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getEmail());
                    ps.setString(4, user.getUserId());
                }
        );
    }

    public User findByUserId_varargs(String userId) {
        return jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                new ObjectRowMapper<>(User.class), userId);
    }

    public User findByUserId_ps(String userId) {
        return jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                new ObjectRowMapper<>(User.class), ps -> ps.setString(1, userId));
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                new ObjectRowMapper<>(User.class));
    }

    public List<User> findAll_varargs(String name) {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS WHERE name = ?",
                new ObjectRowMapper<>(User.class), name);
    }

    public List<User> findAll_ps(String name) {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS WHERE name = ?",
                new ObjectRowMapper<>(User.class), ps -> ps.setString(1, name));
    }
}
