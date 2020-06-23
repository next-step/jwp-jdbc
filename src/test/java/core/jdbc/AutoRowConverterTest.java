package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("auto row converter")
class AutoRowConverterTest {
    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    private static final RowConverter<User> resultSetToUserConverter =
            resultSet -> new User(
                    resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );

    @BeforeEach
    void init() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("수동 row converter 와 서로 같은지 확인")
    void compareWithManual() {
        User one = findAdmin(resultSetToUserConverter);
        User another = findAdmin(new AutoRowConverter<>(User.class));

        assertThat(one).isEqualTo(another);
    }

    private User findAdmin(RowConverter<User> rowConverter) {
        return jdbcTemplate.findOne(
                "SELECT userId, password, name, email FROM USERS WHERE userId = ?",
                rowConverter,
                "admin"
        );
    }
}