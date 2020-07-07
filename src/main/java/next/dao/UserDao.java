package next.dao;

import core.jdbc.*;
import next.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insertOrUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        // TODO 구현 필요함.
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "UPDATE USERS SET password = ? , name = ?, email= ?  WHERE userId = ?";
        jdbcTemplate.insertOrUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate<User>();
        return selectJdbcTemplate.findAll("SELECT userId, password, name, email FROM USERS", rs -> {
            if (rs.next()) {
                users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email")));
            }
            return users;
        });
    }

    public User findByUserId(String userId) throws SQLException {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate<User>();
        return (User) selectJdbcTemplate.findByUserId("SELECT userId, password, name, email FROM USERS WHERE userid=?", pstmt -> pstmt.setString(1, userId), rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }
}
