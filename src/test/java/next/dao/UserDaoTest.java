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
    private UserDao userDao = new UserDao();
    private final User testUser = new User("userId", "password", "name", "javajigi@email.com");

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void create() {
        // given & when
        userDao.insert(testUser);
        User actual = userDao.findByUserId(testUser.getUserId());
        // then
        assertThat(actual).isEqualTo(testUser);
    }

    @Test
    public void update() {
        // given
        userDao.insert(testUser);
        User expected = userDao.findByUserId(testUser.getUserId());
        expected.update(new User(testUser.getUserId(), "password2", "name2", "sanjigi@email.com"));
        // when
        userDao.update(expected);
        User actual = userDao.findByUserId(testUser.getUserId());
        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() {
        // given & when
        UserDao userDao = new UserDao();
        List<User> users = userDao.findAll();
        // then
        assertThat(users).hasSize(1);
    }
}