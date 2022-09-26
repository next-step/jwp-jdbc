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
        final InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate() {
            @Override
            void setValuesForInsert(User user, PreparedStatement pstmt) {
                try {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                } catch (SQLException e) {
                    throw new RuntimeException("fail to set value to preparedstatement");
                }
            }

            @Override
            String createQueryForInsert() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }
        };
        insertJdbcTemplate.insert(user, this);
    }

    public void update(User user) {
        final UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate() {
            @Override
            void setValuesForUpdate(User user, PreparedStatement pstmt) {
                try {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getUserId());
                } catch (SQLException e) {
                    throw new RuntimeException("fail to set value to preparedstatement");
                }
            }

            @Override
            String createQueryForUpdate() {
                return "UPDATE USERS SET password = ?, name = ? , email = ? WHERE userId = ?";
            }
        };
        updateJdbcTemplate.update(user, this);
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
            final ArrayList<User> users = new ArrayList<>();

            while(rs.next()) {
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
