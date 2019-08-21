package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate.command(con -> {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            return pstmt;
        });
    }

    public void update(User user) {
        JdbcTemplate.command(con -> {
            String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
            return pstmt;
        });
    }

    public List<User> findAll() throws SQLException {

        return JdbcTemplate.query(con -> {
            String sql = "SELECT userId, password, name, email FROM USERS";
            PreparedStatement pstmt = con.prepareStatement(sql);
            return pstmt;
        }, rs -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
                users.add(user);
            }
            return users;
        });

    }

    public User findByUserId(String userId) {
        return JdbcTemplate.query(con -> {
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            return pstmt;
        }, rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }
}
