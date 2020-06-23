package next.dao;

import core.jdbc.CommonJdbc;
import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        final CommonJdbc commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        commonJdbc.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        final CommonJdbc commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "UPDATE users SET password=?, name=?, email=? WHERE userId=?";
        commonJdbc.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        final CommonJdbc commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "SELECT userId, password, name, email FROM users";
        return commonJdbc.query(
                sql,
                (rs, rowNum) -> new User(
                        rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email")),
                null);
    }

    public User findByUserId(String userId) throws SQLException {
        final CommonJdbc commonJdbc = new CommonJdbc(ConnectionManager.getDataSource());
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return commonJdbc.queryForSingleObject(sql,
                (rs, rowNum) -> new User(
                        rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email")),
                userId);
    }
}
