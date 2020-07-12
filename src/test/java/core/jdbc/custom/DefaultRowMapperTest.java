package core.jdbc.custom;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultRowMapperTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    @Test
    void mapRowForObject() throws Exception {
        final User expected = new User("admin", "password", "자바지기", "admin@slipp.net");
        final PreparedStatement preparedStatement = getPreparedStatement("select * from USERS");
        final ResultSet resultSet = preparedStatement.executeQuery();

        DefaultRowMapper<User> userDefaultRowMapper = new DefaultRowMapper<>(User.class);
        final User user = userDefaultRowMapper.mapRowForObject(resultSet);

        assertThat(expected).isEqualTo(user);
    }

}
