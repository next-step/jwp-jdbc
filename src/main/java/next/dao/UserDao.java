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
        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcContext.executeUpdate(sql);
    }

    public void update(User user) throws SQLException {
        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUserId());
            }
        };

        final String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";
        jdbcContext.executeUpdate(sql);
    }

    public List<User> findAll() throws SQLException {
        final String sql = findAllSql();

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

    private String findAllSql() {
        return "SELECT userId, password, name, email FROM USERS";
    }

    public User findByUserId(String userId) throws SQLException {
        final String sql = findSql();

        try (final Connection con = ConnectionManager.getConnection();
             final PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            final ResultSet rs = pstmt.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        }
    }

    private String findSql() {
        return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    }
}
