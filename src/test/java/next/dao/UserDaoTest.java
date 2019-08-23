package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.DataAccessException;
import core.jdbc.JdbcTemplate;
import next.model.User;
import org.junit.jupiter.api.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @Order(1)
    public void crud() throws DataAccessException {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update(expected, "userId");
        actual = userDao.findByUserIdSetter(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Order(2)
    public void findAll() throws DataAccessException {
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}