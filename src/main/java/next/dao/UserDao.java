package next.dao;

import core.db.JdbcTemplate;
import core.db.RowMapper;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=? ";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.executeQuery(sql, getUserRowMapper());
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.executeQueryForObject(sql, pstmt -> pstmt.setString(1, userId), getUserRowMapper());
    }

    private RowMapper<User> getUserRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mappedRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };
    }
}
