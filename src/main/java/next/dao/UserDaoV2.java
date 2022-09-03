package next.dao;

import core.jdbc.JdbcTemplateV2;
import core.jdbc.RowMapperV2;
import next.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDaoV2 {

    public void insert(User user) {
        JdbcTemplateV2.getInstance().execute("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplateV2.getInstance().execute("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return JdbcTemplateV2.getInstance()
                .query("SELECT * FROM USERS", this.rowMapper())
                .stream()
                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId) {
        return JdbcTemplateV2.getInstance()
                .querySingle("SELECT userId, password, name, email FROM USERS WHERE userId = ?", this.rowMapper(), userId);
    }

    private RowMapperV2<User> rowMapper() {
        return resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }

}
