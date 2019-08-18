package next.dao;

import core.jdbc.JdbcContext;
import core.jdbc.ResultSetMapper;
import next.model.User;

import java.util.List;

public class NewUserDao implements UserDao {

    private final JdbcContext jdbcContext = new JdbcContext();

    private final ResultSetMapper<User> rsMapper = rs -> new User(
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
        return jdbcContext.execute("SELECT userId, password, name, email FROM USERS", User.class);
    }

    public List<User> findAllByRsMapper() {
        return jdbcContext.execute("SELECT userId, password, name, email FROM USERS", rsMapper);
    }

    public User findByUserId(String userId) {
        return jdbcContext.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", User.class, userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("[" + userId + "] user does not exist");
                });
    }

    public User findByUserIdByRsMapper(String userId) {
        return jdbcContext.executeOne("SELECT userId, password, name, email FROM USERS WHERE userId = ?", rsMapper, userId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("[" + userId + "] user does not exist");
                });
    }
}
