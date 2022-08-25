package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementCreator;
import core.jdbc.ResultSetExtractor;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate = JdbcTemplate.INSTANCE;

    public void insert(User user) throws SQLException {
        jdbcTemplate.executeUpdate(con -> {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            return pstmt;
        });
    }

    public void update(User user) throws SQLException {
        jdbcTemplate.executeUpdate(con -> {
            String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
            return pstmt;
        });
    }

    public List<User> findAll() throws SQLException {
        return (List<User>) jdbcTemplate.executeQueryForObject(
            con -> {
                String sql = "SELECT * FROM USERS";
                return con.prepareStatement(sql);
            },
            rs -> {
                List<User> users = new ArrayList<>();
                if (rs.next()) {
                    users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email")));
                }

                return users;
            });
    }

    public User findByUserId(String userId) throws SQLException {
        return (User) jdbcTemplate.executeQueryForObject(con -> {
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            return pstmt;
        },
            rs -> {
                User user = null;
                if (rs.next()) {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                }

                return user;
        });
    }

}
