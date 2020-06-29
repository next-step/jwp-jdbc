package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::createUser);
    }

    public User findByUserId(String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return jdbcTemplate.queryForObject(sql, this::createUser, userId);
    }

    private User createUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
    }
}
