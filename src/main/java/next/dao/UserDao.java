package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) throws SQLException {
        jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        jdbcTemplate.execute("UPDATE USERS SET userId=?, password=?, name=?, email=? WHERE userid=?",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.queryForList("SELECT userId, password, name, email FROM USERS",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")));

    }

    public User findByUserId(String userId) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                userId);
    }
}
