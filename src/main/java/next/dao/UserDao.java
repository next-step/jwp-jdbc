package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        RowMapper<User> rowMapper = rs ->
                new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
        return jdbcTemplate.selectForList(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        RowMapper<User> rowMapper = rs ->
            new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
        return jdbcTemplate.selectForObject(sql, rowMapper, userId);
    }
}
