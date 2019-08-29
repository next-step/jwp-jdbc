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

public class UserDaoTest {
    @BeforeEach
    public void setup() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        final User expected = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update(expected);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        final UserDao userDao = new UserDao();
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    public void findAllOther() throws Exception {
        final User user = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insert(user);
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    public void delete() throws Exception {
        final User user = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insert(user);
        userDao.delete(user);
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }
}