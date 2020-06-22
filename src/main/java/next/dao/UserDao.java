package next.dao;

import core.jdbc.JdbcApi;
import next.model.User;

import java.util.List;

public class UserDao {
    private final JdbcApi<User> userDB = new JdbcApi<>(User.class);

    public void insert(User user) {
        userDB.execute(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        userDB.execute(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return userDB.findAll("SELECT userId, password, name, email FROM USERS");
    }

    public User findByUserId(String userId) {
        return userDB.findOne("SELECT userId, password, name, email FROM USERS WHERE userid=?", userId);
    }
}
