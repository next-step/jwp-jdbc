package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author KingCjy
 */
public class TransactionTest {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        dataSource = ConnectionManager.getDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void TransactionTest() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);

        try {
            User user = findByUserId("KingCjy");
            updateEmailFail(user.getUserId(), "dododododo@dodo.dodo");
            connection.commit();
        } catch (DataAccessException e) {
            try {
                connection.rollback();
                logger.info("Rollback Called");
            } catch (SQLException ex) {
                logger.error("SQLException", ex);
            }
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
            DataSourceUtils.releaseConnection(connection);
            logger.info("Release Connection");
        }

        assertThat(findByUserId("KingCjy")).isNotEqualTo("dododododo@dodo.dodo");
    }

    public User findByUserId(String userId) {
        String query = "SELECT * FROM USERS WHERE userId = ?";
        User user = jdbcTemplate.queryForObject(query, getUserMapper(), userId);

        return user;
    }

    public int updateEmailFail(String userId, String email) {
        String query = "UPDATE USERS SET email = ? WHERE userIda = ?";
        return jdbcTemplate.update(query, email, userId);
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
