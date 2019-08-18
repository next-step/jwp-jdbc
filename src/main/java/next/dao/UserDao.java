package next.dao;

import core.jdbc.JdbcTemplate;
import next.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Object[] params = new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()};
        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.update(sql, params, types);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        Object[] params = new Object[]{user.getPassword(), user.getName(), user.getEmail(), user.getUserId()};
        int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.update(sql, params, types);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        Object[] params = new Object[]{};
        int[] types = new int[]{};
        return jdbcTemplate.selectList(sql
                , params
                , types
                , this::mappingToUser);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        Object[] params = new Object[]{userId};
        int[] types = new int[]{Types.VARCHAR};
        return jdbcTemplate.select(sql
                , params
                , types
                , this::mappingToUser);

    }

    public User mappingToUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
    }
}
