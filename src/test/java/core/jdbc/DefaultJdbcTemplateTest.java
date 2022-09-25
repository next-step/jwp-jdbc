package core.jdbc;

import next.dao.UserRowMapper;
import next.model.User;
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
    private static final String USER_SELECT_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    private static final String USER_UPDATE_QUERY = "UPDATE USERS SET name = ? WHERE userid=?";
    private static final String USER_DELETE_QUERY = "DELETE FROM USERS WHERE userid=?";

    public static final String USERID = "hun2";
    public static final String PASSWORD = "1q2w3e4r";
    public static final String NAME = "lee";
    public static final String EMAIL = "123@123.com";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new DefaultJdbcTemplate();
    }

    @DisplayName("insert")
    @Test
    void insert() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        User user = jdbcTemplate.get(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("update")
    @Test
    void update() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        String newName = "updated";
        jdbcTemplate.update(USER_UPDATE_QUERY, newName, USERID);

        User user = jdbcTemplate.get(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("delete")
    @Test
    void delete() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);

        jdbcTemplate.update(USER_DELETE_QUERY, USERID);

        User user = jdbcTemplate.get(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNull();
    }

    @DisplayName("get - 쿼리 결과를 RowMapper를 통해 객체로 반환한다.")
    @Test
    void get() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);
        User user = jdbcTemplate.get(USER_SELECT_QUERY, new UserRowMapper(), USERID);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(USERID);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
    }

    @DisplayName("findAll 메서드는 뭐리의 결과를 목록으로 반환한다.")
    @Test
    void findAll() {
        jdbcTemplate.update(USER_INSERT_QUERY, USERID, PASSWORD, NAME, EMAIL);
        jdbcTemplate.update(USER_INSERT_QUERY, USERID+"1", PASSWORD, NAME, EMAIL);
        jdbcTemplate.update(USER_INSERT_QUERY, USERID+"2", PASSWORD, NAME, EMAIL);


        List<User> users = jdbcTemplate.findAll("SELECT * FROM USERS", new UserRowMapper(), DefaultPreparedStatementSetter.empty());

        logger.info("users: {}", users);
        assertThat(users).hasSize(4);
    }
}