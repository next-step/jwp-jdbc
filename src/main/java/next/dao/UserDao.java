package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;
import java.util.Map;

public class UserDao {

    private static final JdbcTemplate jdbcTemplate = JdbcTemplate.instance();

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", Map.of(
                1, user.getUserId(),
                2, user.getPassword(),
                3, user.getName(),
                4, user.getEmail()
        ));
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?", Map.of(
                1, user.getPassword(),
                2, user.getName(),
                3, user.getEmail(),
                4, user.getUserId()
        ));
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS", User.class);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.queryForObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?",
                User.class,
                Map.of(1, userId)
        ).orElse(null);
    }
}
