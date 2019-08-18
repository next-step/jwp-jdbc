package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    public void insert(User user) {
        executeSql(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, user.getUserId());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getName());
                    ps.setString(4, user.getEmail());
                    ps.executeUpdate();
                },
                rs -> null
        );
    }

    public void update(User user) {
        executeSql(
                "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                ps -> {
                    ps.setString(1, user.getPassword());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getEmail());
                    ps.setString(4, user.getUserId());
                    ps.executeUpdate();
                },
                rs -> null
        );
    }

    public List<User> findAll() {
        return executeSql(
                "SELECT userId, password, name, email FROM USERS",
                ps -> {},
                rs -> {
                    List<User> users = new ArrayList<>();
                    if (rs.next()) {
                        User user = new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"));
                        users.add(user);
                    }
                    return users;
                }
        ).orElse(new ArrayList<>());
    }

    public Optional<User> findByUserId(String userId) {
        return executeSql(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?",
                ps -> ps.setString(1, userId),
                rs -> {
                    User user = null;
                    if (rs.next()) {
                        user = new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"));
                    }
                    return user;
                }
        );
    }

    private <T> Optional<T> executeSql(String sql, SqlExecuteStrategy excuteStrategy, SqlResultStrategy<T> resultStrategy) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            excuteStrategy.executeSql(ps);

            if (sql.contains("SELECT")) {
                ResultSet rs = ps.executeQuery();
                return Optional.of(resultStrategy.resultSql(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new SqlException("UserDao executeSql failed: ", e);
        }
    }
}
