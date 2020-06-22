package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.resultset.UserRowMapper;
import core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        int insertCount = jdbcTemplate.update(
            "INSERT INTO USERS VALUES (?, ?, ?, ?)",
            user.getUserId(),
            user.getPassword(),
            user.getName(),
            user.getEmail()
        );

        log.debug("insertCount: {}", insertCount);
    }

    public void update(User user) {
        int updateCount = jdbcTemplate.update(
            "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getUserId()
        );

        log.debug("updateCount: {}", updateCount);
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        log.debug("user: {}", StringUtils.toPrettyJson(users));

        return users;
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";

        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
        log.debug("user: {}", StringUtils.toPrettyJson(user));

        return user;
    }
}
