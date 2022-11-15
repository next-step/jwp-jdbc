package next.dao;

import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }
        };

        jdbcTemplate.update(
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) throws SQLException {
        final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

        };
        jdbcTemplate.update(ps -> {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() throws SQLException {
        final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }
        };
        return jdbcTemplate.query(new RowMapperImpl(User.class));
    }

    public User findByUserId(String userId) throws SQLException {
        final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            }
        };

        return jdbcTemplate.queryForObject(
                new RowMapperImpl(User.class),
                ps -> ps.setString(1, userId)
        );
    }
}
