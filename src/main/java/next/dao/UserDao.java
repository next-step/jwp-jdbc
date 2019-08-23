package next.dao;

import next.jdbc.JdbcTemplate;
import next.jdbc.TypeMapper;
import next.model.User;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final TypeMapper<User> userTypeMapper = new TypeMapper<>(User.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.execute(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.execute(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.queryList(sql, userTypeMapper);
    }

    public Optional<User> findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.querySingle(sql, userTypeMapper, userId);
    }
}
