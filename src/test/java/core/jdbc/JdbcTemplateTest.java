package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

class JdbcTemplateTest {

    private static final String INSERT_SQL = "insert into users (userId, password, name, email) values (?, ?, ?, ?)";

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("정상 쿼리 실행")
    @Test
    void execute() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        final int actual = jdbcTemplate.execute(INSERT_SQL, "userId", "password", "name", "email");

        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("예외 발생 시 Unchecked 예외를 던진다")
    @Test
    void throws_unchecked_exception() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();

        assertThatThrownBy(() -> jdbcTemplate.execute(INSERT_SQL)).isInstanceOf(JdbcTemplateException.class);
    }
}
