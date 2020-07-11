package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = rs ->
            new User(rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"));


    public UserDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) {
        jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.execute("UPDATE USERS SET userId=?, password=?, name=?, email=? WHERE userid=?",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.queryForList("SELECT userId, password, name, email FROM USERS", userRowMapper);

    }

    public User findByUserId(String userId) {
        Optional<User> user = jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                userRowMapper, userId);
        return user.orElseThrow(IllegalArgumentException::new);
    }

    public void deleteAll() {
        jdbcTemplate.execute("DELETE FROM USERS");
    }
}
