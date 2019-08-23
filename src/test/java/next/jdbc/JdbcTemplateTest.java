package next.jdbc;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JdbcTemplateTest {

    private JdbcTemplate jdbcTemplate;
    private User defaultUser;

    @BeforeEach
    void setUp() {
        final DataSource dataSource = ConnectionManager.getDataSource();
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource);
        defaultUser = new User("jaeyeonling", "P@ssw0!d",
                "Jaeyeon Kim", "jaeyeonling@gmail.com");
    }

    @DisplayName("Insert 시 데이터가 들어간다.")
    @Test
    void insert() {
        // when
        insert(defaultUser);
        final User findUser = findUser(defaultUser.getUserId()).orElseThrow();

        // then
        assertThat(findUser.getUserId()).isEqualTo(defaultUser.getUserId());
        assertThat(findUser.getPassword()).isEqualTo(defaultUser.getPassword());
        assertThat(findUser.getName()).isEqualTo(defaultUser.getName());
        assertThat(findUser.getEmail()).isEqualTo(defaultUser.getEmail());
    }

    @DisplayName("Update 시 데이터가 수정된다.")
    @Test
    void update() {
        // given
        final String name = "Matt";
        final String sql = "UPDATE USERS SET name=? WHERE userId=?";
        insert(defaultUser);

        // when
        jdbcTemplate.execute(sql, name, defaultUser.getUserId());
        final User updateUser = findUser(defaultUser.getUserId()).orElseThrow();

        // then
        assertThat(updateUser.getName()).isEqualTo(name);
    }

    @DisplayName("파라미터 갯수가 적으면 예외처리 한다.")
    @Test
    void lessParameters() {
        assertThatExceptionOfType(JdbcTemplateException.class)
                .isThrownBy(() -> jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?, ?, ?)",
                        "userId", "password", "name", "email"));
    }

    @DisplayName("파라미터 갯수가 많으면 예외처리 한다.")
    @Test
    void moreParameters() {
        assertThatExceptionOfType(JdbcTemplateException.class)
                .isThrownBy(() -> jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?)",
                        "userId", "password", "name", "email"));
    }

    @DisplayName("데이터를 조회한다.")
    @Test
    void querySingle() {
        // given
        insert(defaultUser);

        // when
        final User findUser = findUser(this.defaultUser.getUserId()).orElseThrow();

        // then
        assertThat(findUser.getUserId()).isEqualTo(defaultUser.getUserId());
        assertThat(findUser.getPassword()).isEqualTo(defaultUser.getPassword());
        assertThat(findUser.getName()).isEqualTo(defaultUser.getName());
        assertThat(findUser.getEmail()).isEqualTo(defaultUser.getEmail());
    }

    @DisplayName("데이터를 없을 경우 빈 옵셔널을 반환한다.")
    @Test
    void querySingleNotFound() {
        // when
        Optional<User> user = findUser(this.defaultUser.getUserId());

        // then
        assertThat(user.isEmpty()).isTrue();
    }

    @DisplayName("리스트를 반환한다.")
    @Test
    void queryList() {
        // given
        final int defaultSize = findUsers().size();
        final int count = 10;
        for (int i = 0; i < count; i++) {
            insert(new User("userId" + i, "password" + i, "name" + i, "email" + i));
        }

        // when
        final List<User> users = findUsers();

        // then
        assertThat(users).hasSize(defaultSize + count);
    }

    private void insert(final User user) {
        jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    private Optional<User> findUser(final String userId) {
        return jdbcTemplate.querySingle("SELECT * FROM USERS WHERE userId=?",
                new TypeMapper<>(User.class), userId);
    }

    private List<User> findUsers() {
        return jdbcTemplate.queryList("SELECT * FROM USERS", new TypeMapper<>(User.class));
    }
}