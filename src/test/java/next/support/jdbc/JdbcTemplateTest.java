package next.support.jdbc;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTest {

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
    private final String userId = "test";
    private final String password = "testPassword";
    private final String name = "testName";
    private final String email = "testEmail";

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("유저를 생성한다.")
    void createUser() {
        // given
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        // when
        jdbcTemplate.update(sql, userId, password, name, email);
    }

    @Test
    @DisplayName("유저를 생성한다. - PreparedStatementSetter 사용")
    void createUserByPreparedStatementSetter() {
        // given
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        // when
        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, userId);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, email);
        });
    }

    @Test
    @DisplayName("유저 생성 후 생성된 유저를 조회한다.")
    void findUser() {
        // given
        createUser();

        // when
        User user = findUser(userId);

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저 생성 후 생성된 유저를 조회한다. - PreparedStatementSetter 사용")
    void findUserByPreparedStatementSetter() {
        // given
        createUserByPreparedStatementSetter();

        // when
        User user = findUserByPreparedStatementSetter(userId);

        // then
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저 생성 후 생성된 유저를 수정한다.")
    void updateUser() {
        // given
        createUser();
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        String updatePassword = "newPassword";
        String updateName = "updateName";
        String updateEmail = "updateEmail@test.com";

        // when
        jdbcTemplate.update(sql, updatePassword, updateName, updateEmail, userId);

        // then
        User user = findUser(userId);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(updatePassword);
        assertThat(user.getName()).isEqualTo(updateName);
        assertThat(user.getEmail()).isEqualTo(updateEmail);
    }

    @Test
    @DisplayName("유저 생성 후 생성된 유저를 수정한다. - PreparedStatementSetter 사용")
    void updateUserByPreparedStatementSetter() {
        // given
        createUserByPreparedStatementSetter();
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        String updatePassword = "newPassword";
        String updateName = "updateName";
        String updateEmail = "updateEmail@test.com";

        // when
        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, updatePassword);
            ps.setString(2, updateName);
            ps.setString(3, updateEmail);
            ps.setString(4, userId);
        });

        // then
        User user = findUserByPreparedStatementSetter(userId);
        assertThat(user.getUserId()).isEqualTo(userId);
        assertThat(user.getPassword()).isEqualTo(updatePassword);
        assertThat(user.getName()).isEqualTo(updateName);
        assertThat(user.getEmail()).isEqualTo(updateEmail);
    }

    @Test
    @DisplayName("유저 리스트를 가져온다.")
    void findUsers() {
        // given
        createUser();
        String sql = "SELECT userId, password, name, email FROM USERS";
        // when
        List<User> users = jdbcTemplate.query(sql, userRowMapper());

        // then
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 리스트를 가져온다. - PreparedStatementSetter 사용")
    void findUsersByPreparedStatementSetter() {
        // given
        createUserByPreparedStatementSetter();
        String sql = "SELECT userId, password, name, email FROM USERS";
        // when
        List<User> users = jdbcTemplate.query(sql, userRowMapper());

        // then
        assertThat(users.size()).isEqualTo(2);
    }

    private User findUser(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, userRowMapper(), userId);
    }

    private User findUserByPreparedStatementSetter(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, userRowMapper(), ps -> ps.setString(1, userId));
    }

    private RowMapper<User> userRowMapper() {
        return resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }
}
