package next.dao;

import next.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcContext.executeUpdate(sql);
    }

    public void update(User user) throws SQLException {
        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUserId());
            }
        };

        final String sql = "UPDATE USERS SET name=?, email=? WHERE userid=?";
        jdbcContext.executeUpdate(sql);
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS";

        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) {}
        };

        return jdbcContext.executeQuery(sql, resultSet -> {
            final List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                final User user = new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));

                users.add(user);
            }

            return users;
        });
    }

    public User findByUserId(String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        final JdbcContext jdbcContext = new JdbcContext() {
            @Override
            void setPreparedStatement(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };

        return jdbcContext.executeQuery(sql, resultSet -> {
            User user = null;

            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }

            return user;
        });
    }
}
