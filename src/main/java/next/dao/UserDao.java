package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate<User> jdbcTemplate;

    public UserDao(JdbcTemplate<User> jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                pstmt -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                });
    }

    public void insertV2(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                pstmt -> {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getUserId());
                });
    }

    public void updateV2(User user) {
        jdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                pstmt -> {
                },
                this::toUser);
    }

    public List<User> findAllV2() {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS", this::toUser);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                pstmt -> pstmt.setString(1, userId), this::toUser);
    }

    public User findByUserIdV2(String userId) {
        return jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                this::toUser, userId);
    }

    private User toUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email"));
    }
}
