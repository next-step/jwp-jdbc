package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) {
                try {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                } catch (SQLException e) {
                    throw new RuntimeException("fail to set value to preparedstatement");
                }
            }

            @Override
            Object mapRow(ResultSet resultSet) {
                return null;
            }
        };
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }

    public void update(User user) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) {
                try {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getUserId());
                } catch (SQLException e) {
                    throw new RuntimeException("fail to set value to preparedstatement");
                }
            }

            @Override
            Object mapRow(ResultSet resultSet) {
                return null;
            }
        };
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ? , email = ? WHERE userId = ?");
    }

    public List<User> findAll() throws SQLException {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate<>() {
            @Override
            void setValues(PreparedStatement pstmt) {}

            @Override
            Object mapRow(ResultSet rs) {
                try {
                    return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email"));
                } catch (SQLException e) {
                    throw new RuntimeException("fail to conver resultSet to dao list");
                }
            }
        };

        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS");
    }

    public User findByUserId(String userId) throws SQLException {
        final JdbcTemplate<Object> jdbcTemplate = new JdbcTemplate<>() {
            @Override
            void setValues(PreparedStatement pstmt) {
                try {
                    pstmt.setString(1, userId);
                } catch (SQLException e) {
                    throw new RuntimeException("fail to set preparedstatement");
                }
            }

            @Override
            Object mapRow(ResultSet rs) {
                try {
                    return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email"));
                } catch (SQLException e) {
                    throw new RuntimeException("fail to convert resultset to dao");
                }
            }
        };

        return (User) jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?");
    }
}
