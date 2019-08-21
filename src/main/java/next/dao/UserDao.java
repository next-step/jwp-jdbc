package next.dao;

import next.jdbc.JdbcTemplate;
import next.jdbc.ResultMapper;
import next.jdbc.StatementSupplier;
import next.model.User;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final StatementSupplier supplier = preparedStatement -> {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
        };

        jdbcTemplate.execute(sql, supplier);
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        final StatementSupplier supplier = preparedStatement -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
        };

        jdbcTemplate.execute(sql, supplier);
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        final ResultMapper<List<User>> mapper = resultSet -> {
            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                final String userId = resultSet.getString("userId");
                final String password = resultSet.getString("password");
                final String name = resultSet.getString("name");
                final String email = resultSet.getString("email");

                users.add(new User(userId, password, name, email));
            }

            return users;
        };

        return jdbcTemplate.query(sql, mapper);
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        final StatementSupplier supplier = preparedStatement -> preparedStatement.setString(1, userId);
        final ResultMapper<User> mapper = resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            final String password = resultSet.getString("password");
            final String name = resultSet.getString("name");
            final String email = resultSet.getString("email");

            return new User(userId, password, name, email);
        };

        return jdbcTemplate.query(sql, supplier, mapper);
    }
}
