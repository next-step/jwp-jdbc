package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;


import java.util.List;

public class UserDao {
    public static final String USER_INSERT_SQL = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    public static final String USER_UPDATE_SQL = "UPDATE USERS SET password = ?, name = ? , email = ? WHERE userId = ?";
    public static final String USER_FIND_ALL_SQL = "SELECT userId, password, name, email FROM USERS";
    public static final String USER_FIND_BY_ID_SQL = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        this.jdbcTemplate.update(USER_INSERT_SQL, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        this.jdbcTemplate.update(USER_UPDATE_SQL, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return this.jdbcTemplate.queryForList(USER_FIND_ALL_SQL, getUserMapper());
    }

    public User findByUserId(String userId) {
        return this.jdbcTemplate.queryForObject(USER_FIND_BY_ID_SQL, getUserMapper(), userId);
    }

    private RowMapper<User> getUserMapper() {
        return rs -> {
            User user = null;
            if (rs != null) {
                user = new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        };
    }
}
