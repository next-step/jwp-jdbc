package next.dao;

import core.jdbc.DefaultPreparedStatementSetter;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES(?, ?, ?, ?);",
                user.getUserId(),user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userid=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM USERS",
                new UserRowMapper(),
                DefaultPreparedStatementSetter.empty());
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.createForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                new UserRowMapper(),
                userId);
    }
}
