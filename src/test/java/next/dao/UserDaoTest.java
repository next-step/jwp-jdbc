package next.dao;

import core.db.jdbc.JdbcTemplate;
import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private static UserDao userDao;
    private static UserDao legacyUserDao;

    @BeforeAll
    public static void init() {
        JdbcTemplate template = new JdbcTemplate();
        userDao = new NewUserDao(template);
        legacyUserDao = new LegacyUserDao();
    }

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("User CRUD")
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

    @DisplayName("User 전체 리스트 조회")
    @Test
    public void findAll() throws SQLException {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @DisplayName("User CRUD(기존 로직)")
    @Test
    public void legacyCrud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        legacyUserDao.insert(expected);
        User actual = legacyUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        legacyUserDao.update(expected);
        actual = legacyUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("User 전체 리스트 조회(기존 로직)")
    @Test
    public void legacyFindAll() throws SQLException {
        List<User> users = legacyUserDao.findAll();
        assertThat(users).hasSize(1);
    }
}