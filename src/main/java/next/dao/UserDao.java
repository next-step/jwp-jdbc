package next.dao;

import core.jdbc.JdbcManager;
import next.model.User;

import java.util.List;

public class UserDao {

    private final JdbcManager jdbcManager = new JdbcManager();

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (#{userId}, #{password}, #{name}, #{email})";
        jdbcManager.insert(sql, user);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET name = #{name}, email = #{email} WHERE userId = #{userId}";
        jdbcManager.update(sql, user);
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcManager.findAll(sql, User.class);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = #{userId}";
        return jdbcManager.findById(sql, userId, User.class);
    }
}
