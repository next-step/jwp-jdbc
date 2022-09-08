package core.jdbc;

import next.mapper.UserRowMapper;
import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class UserRowMapperTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void mapRow() {
        User expected = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail());

        UserRowMapper<User> userRowMapper = new UserRowMapper<>(User.class);
        User actual = null;

        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS")) {
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                actual = userRowMapper.mapRow(resultSet);
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
