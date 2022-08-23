package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;
import java.util.Map;

public class UserDao {

    private static final JdbcTemplate jdbcTemplate = JdbcTemplate.instance();

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (#{userId}, #{password}, #{name}, #{email})", Map.of(
                "userId", user.getUserId(),
                "password", user.getPassword(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password=#{password}, name=#{name}, email=#{email} WHERE userId=#{userId}", Map.of(
                "password", user.getPassword(),
                "name", user.getName(),
                "email", user.getEmail(),
                "userId", user.getUserId()
        ));
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS", User.class);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.queryForObject(
                "SELECT userId, password, name, email FROM USERS WHERE userid=#{userId}",
                User.class,
                Map.of("userId", userId)
        ).orElse(null);
    }
}
