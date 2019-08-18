package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NewUserDaoTest {

    private NewUserDao userDao;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.DATA_SOURCE);
        userDao = new NewUserDao();
    }

    @Test
    public void crud() throws SQLException {
        User expected = new User("jun", "0000", "hyunjun", "homelus@daum.net");
        userDao.insert(expected);
        User actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        User updated = new User("jun", "1111", "layjun", "lay@daum.net");
        userDao.update(updated);
        actual = userDao.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(updated);

        userDao.delete(expected.getUserId());
        assertThrows(IllegalArgumentException.class, () -> userDao.findByUserId(expected.getUserId()));
    }

    @Test
    public void findAll() throws SQLException {
        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    public void findAllByRsMapper() throws SQLException {
        List<User> users = userDao.findAllByRsMapper();
        assertThat(users).hasSize(1);
    }


}
