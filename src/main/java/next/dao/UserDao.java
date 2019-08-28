package next.dao;

import next.jdbc.PreparedStatementParameterSetterWrapper;
import next.jdbc.QueryExecutor;
import next.jdbc.RowMapperWrapper;
import next.jdbc.UpdateExecutor;
import next.model.User;

import java.util.List;

public class UserDao {
    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final UpdateExecutor updateExecutor = new UpdateExecutor();
        updateExecutor.execute(
                sql,
                PreparedStatementParameterSetterWrapper.setParameters((pstmt) -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                }));
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        final UpdateExecutor updateExecutor = new UpdateExecutor();
        updateExecutor.execute(
                sql, PreparedStatementParameterSetterWrapper.setParameters((pstmt) -> {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getUserId());
                }));
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
                }),
                pstmt -> {
                });
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
                PreparedStatementParameterSetterWrapper.setParameters(
                        pstmt -> {
                            pstmt.setString(1, userId);
                        }
                ));
    }
}
