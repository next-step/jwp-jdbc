package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.QuerySetter;
import core.jdbc.RowMapper;
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
        int result = executeUpdate(sql, (pstmt) -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });

        logger.debug("insert - {}", result);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";
        int result = executeUpdate(sql, (pstmt) -> {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUserId());
        });

        logger.debug("update - {}", result);
    }

    public List<User> findAll() throws SQLException {

        String sql = "SELECT userId, password, name, email FROM USERS";

        return executeQuery(sql, (pstmt) -> {
        }, (rs) -> {
            List<User> userList = new ArrayList<>();
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                userList.add(user);
            }
            return userList;
        });
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return executeQuery(sql, (pstmt) -> {
            pstmt.setString(1, userId);
        }, (rs) -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }

    private int executeUpdate(String sql, QuerySetter sqlExecute) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            sqlExecute.queryValues(pstmt);

            return pstmt.executeUpdate();
        }
    }

    private <T> T executeQuery(String sql, QuerySetter sqlExecute, RowMapper<T> mapper) throws SQLException {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            sqlExecute.queryValues(pstmt);


            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.mapping(rs);
            }
        }
    }
}
