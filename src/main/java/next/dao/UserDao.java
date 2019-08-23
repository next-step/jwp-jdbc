package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao implements UserDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = JdbcTemplate.getInstance();
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.execute(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.execute(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.queryForList(sql, this::getUserResultSetExtractor);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return jdbcTemplate.queryForObject(sql, this::getUserResultSetExtractor, userId);
    }

    private User getUserResultSetExtractor(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
    }
}
