package next.dao;

import core.db.jdbc.JdbcTemplate;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NewUserDao implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(NewUserDao.class);

    private JdbcTemplate template;

    public NewUserDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void insert(User user) {
        template.update(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    @Override
    public void update(User user) {
        template.update(
                "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    @Override
    public List<User> findAll() {
        List<User> users = template.query(
                "SELECT userId, password, name, email FROM USERS",
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));

        logger.debug("{}", users);
        return users;
    }

    @Override
    public User findByUserId(String userId) {
        return template.querySingle(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?",
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ), userId)
                .orElse(null);
    }
}
