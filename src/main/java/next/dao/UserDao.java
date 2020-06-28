package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.util.Arrays;
import java.util.List;

public class UserDao {
    private static final String INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid=?";
    private static final String FIND_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    public void insert(User user) throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            List<Object> args = Arrays.asList(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
            jdbcTemplate.update(INSERT_QUERY, args);
        }
    }

    public void update(User user) throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            jdbcTemplate.beginTransaction();
            List<Object> args = Arrays.asList(user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
            jdbcTemplate.update(UPDATE_QUERY, args);
            jdbcTemplate.commit();
        }
    }

    public List<User> findAll() throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            return jdbcTemplate.queryForList(FIND_ALL_QUERY, User.class);
        }
    }

    public User findByUserId(String userId) throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            return jdbcTemplate.queryForObject(FIND_BY_ID, Arrays.asList(userId), User.class);
        }
    }
}
