package next.dao;

import next.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDao {

    private final JdbcContext jdbcContext;

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void insert(User user) throws SQLException {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcContext.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        final String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";
        jdbcContext.executeUpdate(sql, user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcContext.executeForList(sql, resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")));
    }

    public Optional<User> findByUserId(String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcContext.executeForObject(sql, resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"))
                , userId);
    }
}
