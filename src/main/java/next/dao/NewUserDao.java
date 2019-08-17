package next.dao;

import core.jdbc.JdbcContext;
import next.model.User;
import support.exception.FunctionWithException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NewUserDao implements UserDao {

    private final JdbcContext jdbcContext = new JdbcContext();

    private final FunctionWithException<ResultSet, User, SQLException> resultSetMapper = rs -> new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));

    public void insert(User user) {
        jdbcContext.executeUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void delete(String userId) {
        jdbcContext.executeUpdate("DELETE FROM USERS WHERE userId = ?",
                userId);
    }

    public void update(User user) {
        jdbcContext.executeUpdate("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        return jdbcContext.execute("SELECT userId, password, name, email FROM USERS", resultSetMapper);
    }

    public User findByUserId(String userId) {
        return jdbcContext.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", resultSetMapper, userId);
    }
}
