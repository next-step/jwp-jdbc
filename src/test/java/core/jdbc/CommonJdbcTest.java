package core.jdbc;

import core.jdbc.exceptions.DataAccessException;
import next.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommonJdbcTest {

    private static final Logger log = LoggerFactory.getLogger(CommonJdbcTest.class);

    private JdbcOperation commonJdbc;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("userId"), rs.getString("password"),
            rs.getString("name"), rs.getString("email"));

    @BeforeAll
    static void beforeAll() {
        // 처음엔 beforeAll로 처리하려고 했지만 테스트 간 독립적으로 데이터를 유지하기 위해
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @BeforeEach
    void setUp() {
        commonJdbc = new CommonJdbc();
    }

    // todo: 쿼리가 잘못된 경우 TooManyResultSet 같은 예외가 필요할거 같음.
    @DisplayName("쿼리 하나에 오브젝트 하나")
    @Test
    void queryForSingleObject() {
        // 이미 jwp.sql에 넣어둠.
        final User expected = new User("hyeyoom", "1234abcd", "chwon", "neoul_chw@icloud.com");
        final User actual = commonJdbc.queryForSingleObject(
                "SELECT userId, password, name, email FROM users WHERE userid=?",
                userRowMapper,
                "hyeyoom"
        );
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("쿼리 하나에 오브젝트 여럿 ㅇㅅㅇ")
    @Test
    void query() {
        final List<User> users = commonJdbc.query(
                "SELECT userId, password, name, email FROM users",
                userRowMapper,
                (Object[]) null     // well.. it's ugly isn't it??
        );
        assertThat(users.size()).isGreaterThan(0);
    }

    @DisplayName("Insert를 실험해보자.")
    @Test
    void insert() {
        TransactionManager.beginTransaction();
        final User expected = new User("newguy", "1234abcd", "new", "new@noob.io");
        final int affectedRows = commonJdbc.update(
                "INSERT INTO users VALUES (?, ?, ?, ?)",
                expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail()
        );

        log.debug("affected rows: {}", affectedRows);
        assertThat(affectedRows).isEqualTo(1);

        final User actual = commonJdbc.queryForSingleObject(
                "SELECT userId, password, name, email FROM users WHERE userid=?",
                userRowMapper,
                "newguy"
        );
        TransactionManager.commit();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Update를 실험해보자.")
    @Test
    void update() {
        TransactionManager.beginTransaction();
        final User expected = new User("newguy1", "1234abcd", "new", "new@noob.io");
        final int affectedRowsForInsert = commonJdbc.update(
                "INSERT INTO users VALUES (?, ?, ?, ?)",
                expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail()
        );

        log.debug("affected rows for insert: {}", affectedRowsForInsert);
        assertThat(affectedRowsForInsert).isEqualTo(1);

        final int affectedRowsForUpdate = commonJdbc.update(
                "UPDATE users SET name=? WHERE userId=?",
                "hyeyoom", "newguy"
        );
        log.debug("affected rows for update: {}", affectedRowsForInsert);
        assertThat(affectedRowsForUpdate).isEqualTo(1);
        TransactionManager.commit();

        final User actual = commonJdbc.queryForSingleObject(
                "SELECT userId, password, name, email FROM users WHERE userid=?",
                userRowMapper,
                "newguy"
        );
        assertThat(actual.getName()).isEqualTo("hyeyoom");
    }

    @DisplayName("Delete를 실험해보자.")
    @Test
    void delete() {
        final User user = new User("newguy", "1234abcd", "new", "new@noob.io");
        final int affectedRowsForInsert = commonJdbc.update(
                "INSERT INTO users VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail()
        );

        log.debug("affected rows for insert: {}", affectedRowsForInsert);
        assertThat(affectedRowsForInsert).isEqualTo(1);

        final User selectTest = commonJdbc.queryForSingleObject(
                "SELECT userId, password, name, email FROM users WHERE userid=?",
                userRowMapper,
                "newguy"
        );
        log.debug("selectTest: {}", selectTest);
        assertThat(selectTest).isNotNull();

        final int affectedRowsForDelete = commonJdbc.update(
                "DELETE FROM users WHERE userId=?",
                "newguy"
        );
        log.debug("affected rows for delete: {}", affectedRowsForDelete);
        assertThat(affectedRowsForDelete).isEqualTo(1);

        final User actual = commonJdbc.queryForSingleObject(
                "SELECT userId, password, name, email FROM users WHERE userid=?",
                userRowMapper,
                "newguy"
        );
        assertThat(actual).isNull();
    }

    @DisplayName("잘못된 쿼리의 경우 UnableToAccessException 발생")
    @Test
    void exception() {
        assertThatThrownBy(() -> {
            commonJdbc.queryForSingleObject(
                    "SELECT * FROM some_where_i_belong",
                    (rs, rowNum) -> null,
                    ""
            );
        }).isInstanceOf(DataAccessException.class);
    }
}