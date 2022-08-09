package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import core.jdbc.JdbcTemplate;
import next.model.User;

public class UserDao {

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getUserId());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getName());
                ps.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.execute(sql);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? where userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getUserId());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getName());
                ps.setString(4, user.getEmail());
                ps.setString(5, user.getUserId());
            }
        };
        jdbcTemplate.execute(sql);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {

            }
        };

        return jdbcTemplate.query(sql, rs ->
            new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
            )
        );
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, userId);
            }
        };

        return jdbcTemplate.queryForObject(sql, rs ->
            new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
            )
        );
    }
}
