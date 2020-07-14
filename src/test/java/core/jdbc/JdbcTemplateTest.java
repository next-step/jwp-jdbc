package core.jdbc;

import next.exception.DataAccessException;
import next.exception.DuplicateUserIdException;
import next.model.User;
import next.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void exceptionTest() {
        String sql = "SELECT * FROM a";
        assertThatThrownBy(() -> {
            JdbcTemplate.executeQuery(sql, User.class);
        }).isInstanceOf(DataAccessException.class);
    }

    @Test
    public void duplicationExceptionTest() {
        UserService service = new UserService();
        User user = new User("test", "1234", "name1", "email@email.com");
        User user1 = new User("test", "1111", "name2", "email2@email.com");

        assertThatThrownBy(() -> {
            service.insertUser(user);
            service.insertUser(user1);
        }).isInstanceOf(DuplicateUserIdException.class);

    }
}
