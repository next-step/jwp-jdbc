package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import next.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RowMapperFunctionTest extends TestDatabaseSetup {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        super.setup();

        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ? and email = ?";
        connection = ConnectionManager.getConnection();
        preparedStatement = connection.prepareStatement(sql);
    }

    @AfterEach
    void tearDown() throws SQLException {
        preparedStatement.close();
        connection.close();
        resultSet.close();
    }

    @DisplayName("ResultSet 을 원하는 User 타입으로 반환한다")
    @Test
    void return_to_the_user_type() throws SQLException {
        // given
        preparedStatement.setString(1, "admin");
        preparedStatement.setString(2, "admin@slipp.net");

        resultSet = preparedStatement.executeQuery();

        final RowMapperFunction<User> function = (rs) -> {
            rs.next();
            return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
            );
        };

        final User expected = new User("admin", "password", "자바지기", "admin@slipp.net");

        // when
        final User actual = function.apply(resultSet);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("ResultSet 을 String 타입으로 반환한다")
    @Test
    void return_to_the_string_type() throws SQLException {
        // given
        preparedStatement.setString(1, "admin");
        preparedStatement.setString(2, "admin@slipp.net");

        resultSet = preparedStatement.executeQuery();

        final RowMapperFunction<String> function = (rs) -> {
            rs.next();
            return rs.getString("userId");
        };

        final String expected = "admin";

        // when
        final String actual = function.apply(resultSet);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("ResultSet 을 List<String> 타입으로 반환한다")
    @Test
    void return_to_the_list_type() throws SQLException {
        // given
        connection.prepareStatement("INSERT INTO USERS VALUES ('tester', 'password', 'name', 'email')").executeUpdate();

        resultSet = connection.prepareStatement("SELECT * FROM USERS").executeQuery();

        final RowMapperFunction<List<String>> function = (rs) -> {
            List<String> userIds = new ArrayList<>();
            while (rs.next()) {
                userIds.add(rs.getString("userId"));
            }
            return userIds;
        };

        final List<String> expected = Arrays.asList("admin", "tester");

        // when
        final List<String> actual = function.apply(resultSet);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
