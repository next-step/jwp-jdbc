package next.dao;

import next.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private static final RowMapper<User> USER_ROW_MAPPER = User::from;
    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate();

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JDBC_TEMPLATE.update(sql, (pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        }));
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password = ? , name = ? , email = ? WHERE userId = ?";
        JDBC_TEMPLATE.update(sql, (pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        }));
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return JDBC_TEMPLATE.query(sql, USER_ROW_MAPPER);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return JDBC_TEMPLATE.queryForObject(sql,
                pstmt -> pstmt.setString(1, userId),
                USER_ROW_MAPPER);
    }
}
