package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class TransactionTemplateTest {

    private TransactionTemplate transactionTemplate;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = ConnectionManager.getDataSource();

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);

        transactionTemplate = new TransactionTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void transactionTest() {
        transactionTemplate.execute(() -> {
            User user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?", getUserMapper(), "KingCjy");
        });
    }

    @Test
    public void rollbackTest() {
        final String userId = "KingCjy";
        final String changeUserId = "KingCjy2";

        transactionTemplate.execute(() -> {
            jdbcTemplate.update("UPDATE USERS SET userId = ? WHERE userId = ?", changeUserId, userId);
            jdbcTemplate.update("UPDATE USERS SET FAILEDEDEDED", changeUserId, userId);
        });

        User user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE userId = ?", getUserMapper(), "KingCjy");
        assertThat(user.getUserId()).isNotEqualTo(changeUserId);
    }

    private RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
