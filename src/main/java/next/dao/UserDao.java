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

    public void insert(final User user) throws SQLException {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

            preparedStatement.executeUpdate();
        }
    }

    public void update(final User user) throws SQLException {
        final String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());

            preparedStatement.executeUpdate();
        }
    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql);
             final ResultSet resultSet = preparedStatement.executeQuery()) {

            final List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                final String userId = resultSet.getString("userId");
                final String password = resultSet.getString("password");
                final String name = resultSet.getString("name");
                final String email = resultSet.getString("email");

                users.add(new User(userId, password, name, email));
            }

            return users;
        }
    }

    public User findByUserId(final String userId) throws SQLException {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        try (final Connection connection = ConnectionManager.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                final String password = resultSet.getString("password");
                final String name = resultSet.getString("name");
                final String email = resultSet.getString("email");

                return new User(userId, password, name, email);
            }
        }
    }
}
