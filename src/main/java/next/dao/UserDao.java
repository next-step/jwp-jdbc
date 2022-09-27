package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                pstmt -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                });
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ? , email = ? WHERE userId = ?",
                pstmt -> {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getUserId());
                });
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")));
    }

    public User findByUserId(String userId) throws SQLException {
        return (User) jdbcTemplate.queryForObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")),
                pstmt -> pstmt.setString(1, userId));
    }
}
