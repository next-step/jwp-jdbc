package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
 import java.util.Optional;
import next.model.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

class JdbcTemplateTest {

    private static final String INSERT_SQL = "insert into users (userId, password, name, email) values (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("정상 쿼리 실행")
    @Test
    void execute() {
        final int actual = jdbcTemplate.execute(INSERT_SQL, "userId", "password", "name", "email");

        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("예외 발생 시 Unchecked 예외를 던진다")
    @Test
    void throws_unchecked_exception() {
        final ThrowingCallable actual = () -> jdbcTemplate.execute(INSERT_SQL);

        assertThatThrownBy(actual).isInstanceOf(JdbcTemplateException.class);
    }

    @DisplayName("데이터 한 건을 조회한다")
    @Test
    void queryForObject() {
        jdbcTemplate.execute(INSERT_SQL, "userId", "password", "name", "email");

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";

        final Optional<User> actual = jdbcTemplate.queryForObject(sql, userRowMapperFunction(), "userId");

        assertThat(actual).isPresent()
            .get()
            .isEqualTo(new User("userId", "password", "name", "email"));
    }

    @DisplayName("데이터 한 건을 조회 시 일치하는 데이터가 없는 경우 Optional 객체가 리턴된다")
    @Test
    void no_data_queryForObject() {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";

        final Optional<User> actual = jdbcTemplate.queryForObject(sql, userRowMapperFunction(), "userId");

        assertThat(actual).isNotPresent();
    }

    private RowMapperFunction<User> userRowMapperFunction() {
        return resultSet -> new User(
            resultSet.getString("userId"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("email")
        );
    }
}
