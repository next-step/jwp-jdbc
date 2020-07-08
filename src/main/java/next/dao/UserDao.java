package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            int result = pstmt.executeUpdate();
            logger.debug("{}", result);
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUserId());

            int result = pstmt.executeUpdate();
            logger.debug("{}", result);
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT userId, password, name, email FROM USERS";

        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                userList.add(user);
            }
        }
        return userList;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        User user = null;
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = createPreparedStatement(con, sql, userId);
                ResultSet rs = pstmt.executeQuery();

        ) {
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        }
        return user;
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, String userId) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, userId);
        return ps;
    }
}
