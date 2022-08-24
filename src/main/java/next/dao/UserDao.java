package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        jdbcTemplate.update(sql,
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET name = ?, email = ? WHERE userId = ?";
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
        RowMapper<User> rowMapper = getUserRowMapper();

        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate template = JdbcTemplate.getInstance();
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        RowMapper<User> rowMapper = getUserRowMapper();
        return template.queryForObject(sql, rowMapper, userId);
    }

    private RowMapper<User> getUserRowMapper() {
        return rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
    }
}

