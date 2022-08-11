package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private final UserDao userDao = new UserDao(jdbcTemplate);

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void crud() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        int affectedRows = userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
        assertThat(affectedRows).isEqualTo(1);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        affectedRows = userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}
