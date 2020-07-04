package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.PrepareStatementQuery;
import java.sql.SQLException;
import java.util.List;
import next.model.User;

public class UserDao {

    final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) throws SQLException {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.execute(
            new PrepareStatementQuery(sql, user.getUserId(), user.getPassword(), user.getName(),
                user.getEmail()));
    }

    public void update(User user) throws SQLException {
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        this.jdbcTemplate.execute(
            new PrepareStatementQuery(sql, user.getPassword(), user.getName(), user.getEmail(),
                user.getUserId()));
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT * FROM USERS";

        return this.jdbcTemplate.queryList(new PrepareStatementQuery(sql),
            rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")));
    }

    public User findByUserId(String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return this.jdbcTemplate.queryOne(new PrepareStatementQuery(sql, userId),
            rs -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")));
    }
}
