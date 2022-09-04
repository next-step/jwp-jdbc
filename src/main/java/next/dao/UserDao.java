package next.dao;

import next.model.User;
import next.support.jdbc.JdbcTemplate;

import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
    public void insert(User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.queryForList(sql, resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")
        ));
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")
        ), userId);
    }
}
