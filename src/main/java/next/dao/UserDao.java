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
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO USERS VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
        }
    }

    public void update(User user) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?")) {

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
            pstmt.executeUpdate();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        return excuteSql((preparedStatement) -> {}, rs -> {
            if (rs.next()) {
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

    private <T> T excuteSql(SqlExcuteStrategy excuteStrategy, SqlResultStrategy<T> resultStrategy) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement("SELECT userId, password, name, email FROM USERS");
             ResultSet rs = pstmt.executeQuery()) {
            return resultStrategy.resultSql(rs);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @FunctionalInterface
    public interface SqlExcuteStrategy {
        void excuteSql(PreparedStatement pstmt);
    }

    @FunctionalInterface
    public interface SqlResultStrategy<T> {
        T resultSql(ResultSet rs) throws SQLException;
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
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
}
