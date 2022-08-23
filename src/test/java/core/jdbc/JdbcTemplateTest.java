package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import java.util.List;
import java.util.Optional;
import next.model.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateTest extends TestDatabaseSetup {

    private static final String INSERT_SQL = "insert into users (userId, password, name, email) values (?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID_SQL = "SELECT userId, password, name, email FROM USERS where userId = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM USERS where userId = ?";

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    @DisplayName("정상 쿼리 실행")
    @Test
    void execute() {
        final int actual = 유저_생성();

        assertThat(actual).isEqualTo(1);
    }

    private int 유저_생성() {
        return jdbcTemplate.execute(INSERT_SQL, "userId", "password", "name", "email");
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
        final Optional<User> actual = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, userRowMapperFunction(), "admin");

        assertThat(actual).isPresent()
            .get()
            .isEqualTo(new User("admin", "password", "자바지기", "admin@slipp.net"));
    }

    @DisplayName("데이터 한 건을 조회 시 일치하는 데이터가 없는 경우 Optional 객체가 리턴된다")
    @Test
    void no_data_queryForObject() {
        final Optional<User> actual = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, userRowMapperFunction(), "userId");

        assertThat(actual).isNotPresent();
    }

    @DisplayName("데이터 여러 건을 조회한다")
    @Test
    void queryForList() {
        유저_생성();

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

    @DisplayName("데이터 삭제에 성공한 row 수를 반환한다")
    @Test
    void delete_datum() {
        유저_생성();

        final int actual = jdbcTemplate.execute(DELETE_BY_ID_SQL,
            preparedStatement -> preparedStatement.setString(1, "userId"));

        assertThat(actual).isEqualTo(1);
    }

    @DisplayName("삭제한 데이터는 조회할 수 없다")
    @Test
    void not_exists_data() {
        유저_생성();

        // when
        final Optional<User> addedUser = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, userRowMapperFunction(),
            preparedStatement -> preparedStatement.setString(1, "userId"));

        // then
        assertThat(addedUser).isPresent();

        // when
        final int deletedCount = jdbcTemplate.execute(DELETE_BY_ID_SQL,
            preparedStatement -> preparedStatement.setString(1, "userId"));

        // then
        assertThat(deletedCount).isEqualTo(1);

        // when
        final Optional<User> deletedUser = jdbcTemplate.queryForObject(FIND_BY_ID_SQL, userRowMapperFunction(),
            preparedStatement -> preparedStatement.setString(1, "userId"));

        //then
        assertThat(deletedUser).isNotPresent();
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
