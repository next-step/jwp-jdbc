package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                pstmt -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                });
    }

    public void update(User user) throws SQLException {
        jdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                pstmt -> {
                    pstmt.setString(1, user.getName());
                    pstmt.setString(2, user.getEmail());
                    pstmt.setString(3, user.getUserId());
                });
    }

    public List<User> findAll() throws SQLException {
        final List<Object> list = jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                pstmt -> {
                },
                rs -> new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email")));
        return list.stream()
                .map(o -> (User) o)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId) throws SQLException {
        return (User) jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                pstmt -> pstmt.setString(1, userId),
                rs -> new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email")));
    }
}
