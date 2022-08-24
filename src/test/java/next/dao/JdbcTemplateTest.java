package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import static org.assertj.core.api.Assertions.*;
public class JdbcTemplateTest {

    JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void update() {
        String updateSql = "UPDATE USERS SET name = ?, email = ? WHERE userId = ?";
        String userId = "admin";

        jdbcTemplate
                .update(updateSql, "test", "test@gamil.com", userId);

        User expected = selectAssertThat(userId);
        assertUser(expected, userId, "test", "test@gamil.com");
    }

    private User selectAssertThat(String userId) {
        String selectSql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));

        return jdbcTemplate.queryForObject(selectSql, rowMapper, userId);
    }

    @Test
    void insert() {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        String userId = "userId";
        jdbcTemplate.update(sql, userId, "1234", "hwang", "hwang@gamil.com");

        User expected = selectAssertThat(userId);
        assertUser(expected, userId, "hwang", "hwang@gamil.com");
    }

    private void assertUser(User expected, String userId, String name, String email) {
        assertThat(expected.getUserId()).isEqualTo(userId);
        assertThat(expected.getName()).isEqualTo(name);
        assertThat(expected.getEmail()).isEqualTo(email);
    }
}
