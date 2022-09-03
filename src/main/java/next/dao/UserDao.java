package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    public void insert(User user) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }

    public void update(User user) throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUserId());
            }
        };
        jdbcTemplate.update("UPDATE USERS SET name = ?, email = ? WHERE userId = ?");
    }

    public List<User> findAll() throws SQLException {
        final SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) {
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };
        return selectJdbcTemplate.query("SELECT userId, password, name, email FROM USERS").stream()
                .map(o -> (User) o)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId) throws SQLException {
        final SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };
        return (User) selectJdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid = ?");
    }
}
