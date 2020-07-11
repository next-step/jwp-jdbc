package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.exception.JdbcRuntimeException;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        userDao = new UserDao();
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
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
        User user = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(user);
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(2);
    }


    @DisplayName("에러가 발생하면 JdbcRuntimeException이 발생한다.")
    @Test
    void throwJdbcRuntimeException() {
        assertThatExceptionOfType(JdbcRuntimeException.class).isThrownBy(() -> {
            User expected = new User("userId", "password", "name", "javajigi@email.com");
            userDao.insert(expected);
            userDao.insert(expected);
        });
    }

    @DisplayName("User 데이터를 삭제한다.")
    @Test
    void deleteAll() {
        User user = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(user);
        userDao.deleteAll();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(0);
    }

    @DisplayName("userId로 데이터를 찾는다.")
    @Test
    void findByUserId() {
        User user = new User("userId", "password", "name", "javajigi@email.com");
        userDao.insert(user);
        User result = userDao.findByUserId("userId");

        assertThat(result).isEqualTo(user);
    }

    @DisplayName("userId로 조회 시 데이터가 0건이다.")
    @Test
    void findByUserId2() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            User result = userDao.findByUserId("userId");
        });
    }
}
