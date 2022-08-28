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
    private UserDao userDao = new UserDao();
    private final User testUser = new User("userId", "password", "name", "javajigi@email.com");

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("가변인자를 통한 insert 테스트")
    public void create() {
        // given & when
        userDao.insert(testUser);
        User actual = userDao.findByUserId(testUser.getUserId());
        // then
        assertThat(actual).isEqualTo(testUser);
    }

    @Test
    @DisplayName("인터페이스를 통한 insert 테스트")
    public void create_with_interface() {
        // given & when
        userDao.insertWithInterface(testUser);
        User actual = userDao.findByUserIdWithInterface(testUser.getUserId());
        // then
        assertThat(actual).isEqualTo(testUser);
    }

    @Test
    @DisplayName("가변 인자를 통한 update 테스트")
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
    @DisplayName("인터페이스를 통한 update 테스트")
    public void update_with_interface() {
        // given
        userDao.insertWithInterface(testUser);
        User expected = userDao.findByUserIdWithInterface(testUser.getUserId());
        expected.update(new User(testUser.getUserId(), "password2", "name2", "sanjigi@email.com"));
        // when
        userDao.updateWithInterface(expected);
        User actual = userDao.findByUserIdWithInterface(testUser.getUserId());
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