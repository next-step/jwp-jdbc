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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LegacyUserDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.DATA_SOURCE);
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        LegacyUserDao legacyUserDao = new LegacyUserDao();
        legacyUserDao.insert(expected);
        User actual = legacyUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        legacyUserDao.update(expected);
        actual = legacyUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void delete() throws Exception {
        LegacyUserDao legacyUserDao = new LegacyUserDao();
        String userId = "admin";
        assertNotNull(legacyUserDao.findByUserId(userId));
        legacyUserDao.delete(userId);
        assertNull(legacyUserDao.findByUserId(userId));
    }

    @Test
    public void findAll() throws Exception {
        LegacyUserDao legacyUserDao = new LegacyUserDao();
        List<User> users = legacyUserDao.findAll();
        assertThat(users).hasSize(1);
    }
}