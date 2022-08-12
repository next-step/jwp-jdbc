package next.dao;

import core.jdbc.DefaultPreparedStatementSetter;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS VALUES(?, ?, ?, ?);",
                user.getUserId(),user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userid=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM USERS",
                new UserRowMapper(),
                DefaultPreparedStatementSetter.empty());
    }

    public User findByUserId(String userId) throws SQLException {
        return jdbcTemplate.createForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                new UserRowMapper(),
                userId);
    }
}
