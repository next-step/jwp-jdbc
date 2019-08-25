package next.dao;

import core.jdbc.BeanRowMapper;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private static final String INSERT = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "update users set name = ?, email = ? where userId = ?";
    private static final String FIND_ALL = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_USER_ID = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    private final JdbcTemplate jdbcTemplate;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) {
        jdbcTemplate.update(INSERT, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.update(UPDATE, user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.queryForList(FIND_ALL, new BeanRowMapper<>(User.class));
    }

    public Optional<User> findByUserId(String userId) {
        return jdbcTemplate.queryForOptionalObject(FIND_BY_USER_ID, new BeanRowMapper<>(User.class, false), userId);
    }
}
