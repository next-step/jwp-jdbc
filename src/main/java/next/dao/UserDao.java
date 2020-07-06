package next.dao;

import core.jdbc.*;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.insert(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        // TODO 구현 필요함.
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "UPDATE USERS SET password = ? , name = ?, email= ?  WHERE userId = ?";
        jdbcTemplate.insert(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate();
        return selectJdbcTemplate.findAll(new BindResultSet() {
            @Override
            public Object bindResultSet(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email")));
                }
                return users;
            }
        });
    }

    public User findByUserId(String userId) throws SQLException {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate();
        return selectJdbcTemplate.findByUserId(pstmt -> pstmt.setString(1, userId), rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }
}
