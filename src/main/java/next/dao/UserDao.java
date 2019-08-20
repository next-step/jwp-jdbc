package next.dao;

import core.db.JdbcTemplate;
import next.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.insert(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET userId=?, password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.queryForList(sql, getUserRowMapper());
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, getUserRowMapper(), userId);
    }

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, row) ->
                new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"));
    }
}
