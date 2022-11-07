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

            @Override
            public Object mapRow(ResultSet rs) {
                return null;
            }

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());

                pstmt.executeUpdate();
            }
        };

        jdbcTemplate.insertOrUpdate();
    }

    public void update(User user) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

            @Override
            public Object mapRow(ResultSet rs) {
                return null;
            }

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());

                pstmt.executeUpdate();
            }
        };
        jdbcTemplate.insertOrUpdate();
    }

    public List<User> findAll() throws SQLException {

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS";
            }

            @Override
            public Object mapRow(ResultSet rs) {
                User user = null;
                try {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return user;
            }

            @Override
            public void setValues(PreparedStatement pstmt) {

            }
        };

        return jdbcTemplate.query();
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public String createQuery() {
                return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            }

            @Override
            public Object mapRow(ResultSet rs) {
                User user = null;
                try {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return user;
            }

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };

        return jdbcTemplate.queryForObject();
    }
}
