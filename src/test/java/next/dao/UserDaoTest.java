package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.ManyResultQueryException;
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

    UserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        this.userDao = new UserDao(new JdbcTemplate());
    }

    @Test
    public void crud() {
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
    public void findAll() {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @DisplayName("로그인을 테스트 합니다.")
    @Test
    void loginTest() {
        User user = new User("userId1", "password", "name", "javajigi@email.com");
        userDao.insert(user);

        User result = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(result).isEqualTo(user);
    }

    @DisplayName("id가 두개일때 에러를 발생 시킵니다.")
    @Test
    void manyQueryException() {
        userDao.insert(new User("userId1", "password", "name", "javajigi@email.com"));
        userDao.insert(new User("userId2", "password", "name", "javajigi@email.com"));

        assertThatExceptionOfType(ManyResultQueryException.class).isThrownBy(() -> {
            userDao.findByEmailAndPassword("javajigi@email.com", "password");
        });
    }

}