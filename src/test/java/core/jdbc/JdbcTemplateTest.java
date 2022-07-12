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

class JdbcTemplateTest {

    private final static String INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private final static String SELECT_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    private final static String SELECT_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private final static String USER_ID = "userId";
    private final static String PASSWORD = "password";
    private final static String NAME = "username";
    private final static String EMAIL = "userId@gmail.com";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = JdbcTemplate.getInstance();
        jdbcTemplate.update(INSERT_QUERY, USER_ID, PASSWORD, NAME, EMAIL);
    }

    @DisplayName("저장 및 단건 조회 테스트")
    @Test
    void insertAndSelect() {
        User actual = jdbcTemplate.queryForObject(new UserRowMapper(), SELECT_QUERY, USER_ID);

        assertThat(actual).isEqualTo(new User(USER_ID, PASSWORD, NAME, EMAIL));
    }

    @DisplayName("다건 조회 테스트")
    @Test
    void selectAll() {
        List<User> actual = jdbcTemplate.queryForList(new UserRowMapper(), SELECT_ALL_QUERY);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(1)).isEqualTo(new User(USER_ID, PASSWORD, NAME, EMAIL));
    }
}
