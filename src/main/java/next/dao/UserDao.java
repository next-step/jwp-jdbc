package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;

public class UserDao {
    private static final String INSERT_QUERY = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid=?";
    private static final String FIND_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    public void insert(User user) throws Exception {
        Object[] args = new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()};
        update(INSERT_QUERY, args);
    }

    public void update(User user) throws Exception {
        Object[] args = new Object[]{user.getPassword(), user.getName(), user.getEmail(), user.getUserId()};
        update(UPDATE_QUERY, args);
    }

    private void update(String query, Object[] args) throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            jdbcTemplate.beginTransaction();
            jdbcTemplate.update(query, args);
            jdbcTemplate.commit();
        }
    }

    public List<User> findAll() throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
            return jdbcTemplate.query(FIND_ALL_QUERY, rowMapper);
        }
    }

    public User findByUserId(String userId) throws Exception {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));
            return jdbcTemplate.queryForObject(FIND_BY_ID, rowMapper, new Object[]{userId});
        }
    }
}
