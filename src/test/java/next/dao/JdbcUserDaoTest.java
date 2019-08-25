package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcUserDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        JdbcUserDao jdbcUserDao = JdbcUserDao.INSTANCE;
        jdbcUserDao.insert(expected);
        User actual = jdbcUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        jdbcUserDao.update(expected);
        actual = jdbcUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        JdbcUserDao jdbcUserDao = JdbcUserDao.INSTANCE;
        List<User> users = jdbcUserDao.findAll();
        assertThat(users).hasSize(1);
    }
}