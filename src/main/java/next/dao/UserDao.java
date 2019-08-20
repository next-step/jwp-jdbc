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

    public void insert(User user) throws SQLException {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        }
    }

    public void update(User user) throws SQLException {
        final String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";

        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUserId());

            pstmt.executeUpdate();
        }
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS";

        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql);
             final ResultSet rs = pstmt.executeQuery()) {

            final List<User> users = new ArrayList<>();
            while (rs.next()) {
                final User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));

                users.add(user);
            }

            return users;
        }
    }

    public User findByUserId(String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql);
             final ResultSet rs = pstmt.executeQuery()) {

            pstmt.setString(1, userId);

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        }
    }
}
