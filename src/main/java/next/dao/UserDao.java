package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(
                "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail()
        );
    }

    public void update(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update(
                "UPDATE USERS SET name = ?, email = ? WHERE userId = ?",
                user.getName(), user.getEmail(), user.getUserId()
        );
    }

    public List<User> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email")));
            }
            return users;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public User findByUserId(String userId) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.selectObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid = ?",
                User.class, userId);
    }
}
