package next.dao;

import core.jdbc.JdbcTemplate;
import next.mapper.UserRowMapper;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate.getInstance().update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public User findByUserId(String userId) {
        return JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", this.rowMapper(), userId);
    }

    public List<User> findAll() {
        return JdbcTemplate.getInstance()
                .query("SELECT * FROM USERS", this.rowMapper())
                .stream()
                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());
    }

    private RowMapper<User> rowMapper() {
        return new UserRowMapper<User>(User.class);
    }

}
