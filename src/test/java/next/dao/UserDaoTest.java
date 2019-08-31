package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Dao의 레거시 api를 사용하는 crud 테스트")
    @Test
    public void crudLegacy() throws Exception {
        final User expected = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insertWithParameterSetter(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.updateWithParameterSetter(expected);
        actual = userDao.findByUserIdWithMapper(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        final UserDao userDao = new UserDao();
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    public void findAllAfterAddedOne() throws Exception {
        final User user = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insert(user);
        final List<User> users = userDao.findAll();
        assertThat(users).hasSize(2);
    }

    @DisplayName("Dao의 레거시 api를 사용하는 목록 조회 테스트")
    @Test
    public void findAllWithMapperAfterAddedOne() throws Exception {
        final User user = new User("userId", "password", "name", "javajigi@email.com");
        final UserDao userDao = new UserDao();
        userDao.insertWithParameterSetter(user);
        final List<User> users = userDao.findAllWithMapper();
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