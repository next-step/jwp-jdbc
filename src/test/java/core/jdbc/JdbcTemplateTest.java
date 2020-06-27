package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate;
    private User user = new User("parkeeseul", "test", "seul", "parkeeseul@nextstep.com");

    @BeforeEach
    void setUp() throws InterruptedException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate();
    }

    @Test
    void update() {
        String sql = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, Arrays.asList("parkeeseul", "test", "seul", "parkeeseul@nextstep.com"));
    }

    @Test
    void queryForObject() {
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, Arrays.asList(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        User findUser = jdbcTemplate.queryForObject(sql, Arrays.asList(user.getUserId()), User.class);

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void queryForList() {
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, Arrays.asList(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        List<User> findAllUser = jdbcTemplate.queryForList(sql, Arrays.asList(user.getUserId()), User.class);

        assertThat(findAllUser).hasSize(1);
        assertThat(findAllUser.get(0)).isEqualTo(user);
    }

    @Test
    void transaction_test() {
        jdbcTemplate.beginTransaction();
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, Arrays.asList(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
        jdbcTemplate.commit();

        JdbcTemplate jdbcTemplate2 = new JdbcTemplate();
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        List<User> findAllUser = jdbcTemplate2.queryForList(sql, Arrays.asList(user.getUserId()), User.class);

        assertThat(findAllUser).hasSize(1);
        assertThat(findAllUser.get(0)).isEqualTo(user);
    }

    @Test
    void transaction_rollback_test() {
        jdbcTemplate.beginTransaction();
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, Arrays.asList(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
        jdbcTemplate.rollback();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        List<User> findAllUser = jdbcTemplate.queryForList(sql, Arrays.asList(user.getUserId()), User.class);

        assertThat(findAllUser).hasSize(0);
    }
}
