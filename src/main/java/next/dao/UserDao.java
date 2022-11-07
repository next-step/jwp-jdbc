package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }
        };

        jdbcTemplate.insertOrUpdate(ps -> {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());

            ps.executeUpdate();
        });
    }

    public void update(User user) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

        };
        jdbcTemplate.insertOrUpdate(ps -> {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUserId());

            ps.executeUpdate();
        });
    }

    public List<User> findAll() throws SQLException {

        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }

        };

        return jdbcTemplate.query(rs -> {
            User user = null;
            try {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return user;
        });
    }

    public User findByUserId(String userId) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            }
        };

        return jdbcTemplate.queryForObject(rs -> {
            User user = null;
            try {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return user;
        },
                ps -> {
                    ps.setString(1, userId);
                });
    }
}
