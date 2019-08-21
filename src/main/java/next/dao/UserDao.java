package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.SelectJdbcTemplate;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.update(sql);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };
        jdbcTemplate.update(sql);
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

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
            }

            return users;
        } finally {
            close(rs);
            close(con, pstmt);
        }
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                User user = null;
                if (rs.next()) {
                    user = new User(
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email"));
                }
                return user;
            }

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };
        return (User)jdbcTemplate.select(sql);
    }

    private void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    private void close(Connection con, PreparedStatement pstmt) throws SQLException {
        if (pstmt != null) {
            pstmt.close();
        }

        if (con != null) {
            con.close();
        }
    }
}
