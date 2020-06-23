package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        int updated = jdbcTemplate.update(query, new Object[] {user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});
    }

    public void update(User user) throws SQLException {
        String query = "UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? WHERE userId = ?";
        int updated = jdbcTemplate.update(query, new Object[] {user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), user.getUserId()});
    }

    public List<User> findAll() throws SQLException {
        String query = "SELECT * FROM USERS";
        List<User> users = jdbcTemplate.queryForList(query, new Object[]{}, getUserMapper());
        return users;
    }

    public User findByUserId(String userId) {
        String query = "SELECT * FROM USERS WHERE userId=?";
        User user = jdbcTemplate.queryForObject(query, getUserMapper(), userId);

        return user;
    }

    private RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
