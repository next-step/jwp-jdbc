package next.dao;

import next.jdbc.PreparedStatementParameterSetterCreator;
import next.jdbc.QueryExecutor;
import next.jdbc.RowMapperWrapper;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final QueryExecutor updateExecutor = new QueryExecutor();
        updateExecutor.executeUpdate(
                sql,
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void insertWithParameterSetterCreator(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final QueryExecutor updateExecutor = new QueryExecutor();
        updateExecutor.executeUpdate(
                sql,
                PreparedStatementParameterSetterCreator.create(
                        user.getUserId(),
                        user.getPassword(),
                        user.getName(),
                        user.getEmail()));
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        final QueryExecutor updateExecutor = new QueryExecutor();
        updateExecutor.executeUpdate(
                sql,
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public void delete(final User user) {
        final String sql = "DELETE FROM USERS WHERE userId=?";
        final QueryExecutor updateExecutor = new QueryExecutor();
        updateExecutor.executeUpdate(
                sql,
                user.getUserId());
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        final QueryExecutor queryExecutor = new QueryExecutor();
        return queryExecutor.executeQuery(
                sql,
                RowMapperWrapper.mapRow((rs) -> {
                    final User user = new User(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4));
                    return user;
                }));
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        final QueryExecutor queryExecutor = new QueryExecutor();
        return queryExecutor.executeScalar(
                sql,
                (rs) -> {
                    final User user = new User(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4));
                    return user;
                },
                userId);
    }
}
