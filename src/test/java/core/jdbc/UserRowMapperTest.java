package core.jdbc;

import next.mapper.UserRowMapper;
import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.ResultSet;
import java.sql.SQLException;

class UserRowMapperTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void mapRow() throws SQLException {

        ResultSet mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getString("userId")).thenReturn("fistkim101");
        Mockito.when(mockResultSet.getString("password")).thenReturn("12345");
        Mockito.when(mockResultSet.getString("name")).thenReturn("fistkim");
        Mockito.when(mockResultSet.getString("email")).thenReturn("fistkim101@gmail.com");

        UserRowMapper userRowMapper = new UserRowMapper();
        User actual = userRowMapper.mapRow(mockResultSet);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getUserId()).isEqualTo("fistkim101");
        Assertions.assertThat(actual.getPassword()).isEqualTo("12345");
        Assertions.assertThat(actual.getName()).isEqualTo("fistkim");
        Assertions.assertThat(actual.getEmail()).isEqualTo("fistkim101@gmail.com");
    }
}
