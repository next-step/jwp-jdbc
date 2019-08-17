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
import static org.junit.jupiter.api.Assertions.assertNull;

public class NewUserDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.DATA_SOURCE);
    }

    @Test
    public void crud() {
        User expected = new User("jun", "0000", "hyunjun", "homelus@daum.net");
        NewUserDao newUserDao = new NewUserDao();
        newUserDao.insert(expected);
        User actual = newUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        User updated = new User("jun", "1111", "layjun", "lay@daum.net");
        newUserDao.update(updated);
        actual = newUserDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(updated);

        newUserDao.delete(expected.getUserId());
        assertNull(newUserDao.findByUserId(expected.getUserId()));
    }

    @Test
    public void findAll() {
        NewUserDao userDao = new NewUserDao();
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }


}
