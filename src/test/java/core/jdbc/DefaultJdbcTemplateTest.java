package core.jdbc;

import next.dao.UserRowMapper;
import next.model.User;
import org.junit.jupiter.api.Assertions;
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

class DefaultJdbcTemplateTest {
    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcTemplateTest.class);

    private static final String USER_INSERT_QUERY = "INSERT INTO USERS VALUES(?, ?, ?, ?);";
    private static final String USER_UPDATE_QUERY = "UPDATE USERS SET name = ? WHERE userid=?";
    private static final String USER_SELECT_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    private static final String USER_DELETE_QUERY = "DELETE FROM USERS WHERE userid=?";


    public static final String USERID = "catsbi";
    public static final String PASSWORD = "1q2w3e";
    public static final String NAME = "hansol";
    public static final String EMAIL = "catsbi@next.com";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new DefaultJdbcTemplate();
    }

    @DisplayName("update 메서드로 유효한 insert query 를 이용해 회원을 추가할 수 있다.")
    @Test
    void updateInsertTest() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        User user = jdbcTemplate.createForObject(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("update 메서드로 유효한 update query를 이용해 회원 정보를 수정할 수 있다. ")
    @Test
    void updatePatchTest() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        String newName = "updated";
        jdbcTemplate.update(USER_UPDATE_QUERY, newName, USERID);

        User user = jdbcTemplate.createForObject(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("update 메서드로 유효한 delete query를 이용해 회원 정보를 삭제할 수 있다.")
    @Test
    void updateDeleteTest() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        jdbcTemplate.update(USER_DELETE_QUERY, USERID);

        User user = jdbcTemplate.createForObject(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNull();
    }

    @DisplayName("update 메서드로 유효하지 않은 delete query를 이용할 경우 아무 동작도 일어나지 않는다.")
    @Test
    void updateDeleteTestWithNotExistsUserId() {
        Assertions.assertDoesNotThrow(() -> jdbcTemplate.update(USER_DELETE_QUERY, USERID));
    }

    @DisplayName("createPreparedStatementSetter 메서드는 가변인수로 값을 받아 PreparedStatementSetter를 생성해 반환한다")
    @Test
    void createPreparedStatementSetter() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        PreparedStatementSetter pss = jdbcTemplate.createPreparedStatementSetter(USERID);

        User user = jdbcTemplate.createForObject(USER_SELECT_QUERY, new UserRowMapper(), pss);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("createForObject 메서드는 쿼리의 결과를 매개변수로 받은 RowMapper를 통해 매핑된 결과를 반환한다.")
    @Test
    void createForObject() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        User user = jdbcTemplate.createForObject(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("query 메서드는 쿼리의 결과를 목록으로 반환한다.")
    @Test
    void query() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);
        jdbcTemplate.update(USER_INSERT_QUERY, USERID+"1", PASSWORD, NAME, EMAIL);
        jdbcTemplate.update(USER_INSERT_QUERY, USERID+"2", PASSWORD, NAME, EMAIL);


        List<User> users = jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper(), DefaultPreparedStatementSetter.empty());

        logger.info("users: {}", users);
        assertThat(users).hasSize(4);
    }

}
