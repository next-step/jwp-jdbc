package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();


    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.insert(query, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String query = "UPDATE USERS SET password = ?, name = ?, email = ? where userId = ?";
        jdbcTemplate.update(query, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.execute("SELECT userId, password, name, email from USERS", rs -> {
            String userId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(userId, password, name, email);
        });
    }

    public User findByUserId(String id) throws SQLException {
        String query = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        return jdbcTemplate.execute(query, rs -> {
            String userId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(userId, password, name, email);
        }, id);
    }
}
