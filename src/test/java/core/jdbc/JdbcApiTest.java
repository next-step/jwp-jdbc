package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Jdbc api 테스트")
class JdbcApiTest {
    private final JdbcApi<User> userJdbcApi = new JdbcApi<>(User.class);
    private final User nokchaxUser = new User("nokchax", "pw", "녹차", "nokchax@gmail.com");


    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        userJdbcApi.execute(
                "INSERT INTO USERS VALUES(?, ?, ?, ?)",
                nokchaxUser.getUserId(),
                nokchaxUser.getPassword(),
                nokchaxUser.getName(),
                nokchaxUser.getEmail()
        );
    }

    @Test
    @DisplayName("findOne 에서 하나 이상의 결과를 리턴하면 예외를 발생시킨다")
    void findOneThrowException() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> userJdbcApi.findOne("SELECT * FROM USERS"));
    }

    @Test
    @DisplayName("findOne 테스트")
    void findOne() {
        User user = userJdbcApi.findOne("SELECT * FROM USERS WHERE userID = '" + nokchaxUser.getUserId() + "'");

        assertThat(user).isEqualTo(nokchaxUser);
    }

    @Test
    @DisplayName("findAll 테스트")
    void findAll() {
        List<User> users = userJdbcApi.findAll("SELECT * FROM USERS");

        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("findAll 값을 찾지 못하더라도 빈 리스트를 리턴한다")
    void findAllReturnEmptyList() {
        List<User> users = userJdbcApi.findAll("SELECT * FROM USERS WHERE userId = 'someone that not exist'");

        assertThat(users).isNotNull();
        assertThat(users).isEmpty();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("sql 쿼리가 null 일 경우 예외 발생")
    void throwExceptionWhenSqlIsNotValid(final String sql) {
        assertThatExceptionOfType(JdbcApiException.class)
                .isThrownBy(() -> userJdbcApi.execute(sql));
    }
}