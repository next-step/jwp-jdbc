package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import java.util.List;
import java.util.Optional;
import next.model.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

class JdbcTemplateTest {

    private static final String INSERT_SQL = "insert into users (userId, password, name, email) values (?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT userId, password, name, email FROM USERS";

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
        final String sql = SELECT_SQL + " WHERE userid = ?";

        final Optional<User> actual = jdbcTemplate.queryForObject(sql, userRowMapperFunction(), "admin");

        assertThat(actual).isPresent()
            .get()
            .isEqualTo(new User("admin", "password", "자바지기", "admin@slipp.net"));
    }

    @DisplayName("데이터 한 건을 조회 시 일치하는 데이터가 없는 경우 Optional 객체가 리턴된다")
    @Test
    void no_data_queryForObject() {
        final String sql = SELECT_SQL + " WHERE userid = ?";

        final Optional<User> actual = jdbcTemplate.queryForObject(sql, userRowMapperFunction(), "userId");

        assertThat(actual).isNotPresent();
    }

    @DisplayName("데이터 여러 건을 조회한다")
    @Test
    void queryForList() {
        jdbcTemplate.execute(INSERT_SQL, "userId", "password", "name", "email");

        final List<User> actual = jdbcTemplate.queryForList(SELECT_SQL, userRowMapperFunction());

        assertThat(actual).hasSize(2)
            .extracting("userId")
            .containsExactly("admin", "userId");
    }

    @DisplayName("데이터 여러 건을 조회 시 데이터가 없는 경우 빈 컬렉션을 반환한다")
    @Test
    void empty_queryForList() {
        jdbcTemplate.execute("delete from users");

        final List<User> actual = jdbcTemplate.queryForList(SELECT_SQL, userRowMapperFunction());

        assertThat(actual).isEmpty();
    }

    @DisplayName("쿼리 실행 중 예외 발생 시 rollback 된다")
    @Test
    void transaction_rollback() {
        assertThatThrownBy(() -> jdbcTemplate.execute(INSERT_SQL))
            .isInstanceOf(JdbcTemplateException.class);

        final List<User> users = jdbcTemplate.queryForList(SELECT_SQL, userRowMapperFunction());

        assertThat(users).hasSize(1);
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
