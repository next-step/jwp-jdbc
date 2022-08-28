package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import next.model.User;
import org.springframework.util.ReflectionUtils;

public class UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private RowMapper<User> rowMapper = (rs, rowNum) -> {
        Class<User> userClass = User.class;

        User user = new User();
        Field[] fields = userClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, rs.getString(field.getName()));
            field.setAccessible(false);
        }

        return user;
    };

    public void insert(User user) throws SQLException {
        jdbcTemplate.update(
            "INSERT INTO USERS VALUES (?, ?, ?, ?)",
            user.getUserId(), user.getPassword(), user.getName(), user.getEmail()
        );
    }

    public void update(User user) throws SQLException {
        jdbcTemplate.update(
            "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
            user.getPassword(), user.getName(), user.getEmail(), user.getUserId()
        );
    }

    public List<User> findAll() throws SQLException {
        return jdbcTemplate.query(
            "SELECT * FROM USERS",
            rowMapper
        );
    }

    public User findByUserId(String userId) throws SQLException {
        return jdbcTemplate.queryForObject(
            "SELECT userId, password, name, email FROM USERS WHERE userId=?",
            new Object[] { userId },
            rowMapper
        );
    }

}
