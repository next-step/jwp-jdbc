package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.TypeRowMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.selectList(sql, new TypeRowMapper<>(User.class));
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.select(sql, new TypeRowMapper<>(User.class) , userId);
    }

    public User mappingToUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
    }
}
