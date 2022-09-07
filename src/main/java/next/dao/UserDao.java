package next.dao;

import core.jdbc.JdbcTemplateV1;
import next.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    public void insert(User user) {
        JdbcTemplateV1.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplateV1.getInstance().execute("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return JdbcTemplateV1.getInstance()
                .query("SELECT * FROM USERS", User.class)
                .stream()
                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId) {
        return JdbcTemplateV1.getInstance()
                .querySingle("SELECT userId, password, name, email FROM USERS WHERE userId = ?", User.class, userId);
    }

}
