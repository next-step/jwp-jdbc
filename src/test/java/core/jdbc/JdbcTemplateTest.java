package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class JdbcTemplateTest {

    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("userTable.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("PreparedStatement인 SQL의 parameter 개수가 args의 개수와 같은 경우 에러가 발생하지 않는다")
    void sameArgsCount() {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final Object[] args = {1, 2, 3, 4};

        jdbcTemplate.update(sql, args);
    }

    @Test
    @DisplayName("PreparedStatement인 SQL의 parameter 개수가 args의 개수보다 많은 경우 에러가 발생한다")
    void lessArgsCount() {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final Object[] args = {1, 2, 3};

        final Throwable thrown = catchThrowable(() -> jdbcTemplate.update(sql, args));

        assertThat(thrown).isInstanceOf(JdbcTemplateException.class)
                .hasMessageContaining("Parameter \"#4\" is not set");
    }

    @Test
    @DisplayName("PreparedStatement인 SQL의 parameter 개수가 args의 개수보다 적은 경우 에러가 발생한다")
    void moreArgsCount() {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final Object[] args = {1, 2, 3, 4, 5};

        final Throwable thrown = catchThrowable(() -> jdbcTemplate.update(sql, 1, 2, 3, 4, 5));

        assertThat(thrown).isInstanceOf(JdbcTemplateException.class)
                .hasMessageContaining("Invalid value \"5\" for parameter \"parameterIndex\"");
    }

    @Test
    @DisplayName("테이블이 존재하지 않는 경우 에러가 발생한다")
    void notExistTable() {
        final String tableName = "QAPwOE";
        final String sql = "INSERT INTO " + tableName + " VALUES (1)";

        final Throwable thrown = catchThrowable(() -> jdbcTemplate.update(sql));

        assertThat(thrown).isInstanceOf(JdbcTemplateException.class)
                .hasMessageContaining("Table \"" + tableName.toUpperCase() + "\" not found");
    }

    @Test
    @DisplayName("queryForObject 결과가 존재하지 않는 경우 null을 반환한다")
    void queryForObjectNotExist() {
        final String sql = "SELECT * FROM USERS WHERE userId = ?";

        final User result = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(), "9999");

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("query 결과가 존재하지 않는 경우 빈 컬렉션을 반환한다")
    void queryNotExist() {
        final String sql = "SELECT * FROM USERS";

        final List<User> result = jdbcTemplate.query(sql, (rs, rowNum) -> new User());

        assertThat(result).isEmpty();
    }
}