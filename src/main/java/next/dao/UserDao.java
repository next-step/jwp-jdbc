package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) throws SQLException {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        List<User> result = new ArrayList<>();

        Connection con = null;
        Statement stmt = null;
        try {
            con = ConnectionManager.getConnection();
            final String sql = "SELECT * FROM USERS";
            stmt = con.createStatement();

            final ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                final String userId = rs.getString("userId");
                final String password = rs.getString("password");
                final String name = rs.getString("name");
                final String email = rs.getString("email");

                final User user = new User(userId, password, name, email);
                result.add(user);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
        return result;
    }

    public User findByUserId(String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, this::createUser, userId);
    }

    private User createUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
    }
}
