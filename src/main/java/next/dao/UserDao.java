package next.dao;

import com.google.common.collect.Lists;
import core.jdbc.ConnectionManager;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void create(User user) throws SQLException {
        update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        update("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        List<User> users = query(
                "SELECT userId, password, name, email FROM USERS",
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));

        logger.debug("{}", users);
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        return queryOne(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?",
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ), userId);
    }

    private int update(String sql, String... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setString(i + 1, args[i]);
            }

            return pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    private <T> List<T> query(String sql, QueryResultCallback<T> callback, String... args) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                pstmt.setString(i + 1, args[i]);
            }

            rs = pstmt.executeQuery();

            List<T> items = Lists.newArrayList();
            T item = null;

            if (rs.next()) {
                item = callback.apply(rs);
                items.add(item);
            }

            return items;

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

    private <T> T queryOne(String sql, QueryResultCallback<T> cb, String... args) throws SQLException {
        return query(sql, cb, args).get(0);
    }

    @FunctionalInterface
    public interface QueryResultCallback<T> {
        T apply(ResultSet rs) throws SQLException;
    }
}
