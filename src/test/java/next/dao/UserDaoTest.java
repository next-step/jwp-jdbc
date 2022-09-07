package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    enum ParamType {
        VARARGS, PS, NOARGS
    }

    @DisplayName("Dao JdbcTemplate 의 파라미터가 가변 인자, 혹은 PreparedStatementSetter CRUD 테스트")
    @ParameterizedTest
    @CsvSource(value = {
            "VARARGS, PS"
    })
    public void crud(ParamType paramType) {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();

        // create
        if (paramType == ParamType.VARARGS) {
            userDao.insert_varargs(expected);
        } else if (paramType == ParamType.PS) {
            userDao.insert_ps(expected);
        }

        // read
        User actual = paramType == ParamType.VARARGS ? userDao.findByUserId_varargs(expected.getUserId()) : userDao.findByUserId_ps(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        // update
        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        if (paramType == ParamType.VARARGS) {
            userDao.update_varargs(expected);
        } else if (paramType == ParamType.PS) {
            userDao.update_ps(expected);
        }

        // read (check updated)
        actual = paramType == ParamType.VARARGS ? userDao.findByUserId_varargs(expected.getUserId()) : userDao.findByUserId_ps(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Dao JdbcTemplate 의 파라미터가 PreparedStatementSetter 일 때의 CRUD 테스트")
    @Test
    public void crud_ps() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert_ps(expected);
        User actual = userDao.findByUserId_ps(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        userDao.update_ps(expected);
        actual = userDao.findByUserId_ps(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Dao JdbcTemplate 의 파라미터가 가변인자 혹은 PreparedStatementSetter 혹은 인자가 없을 경우 일때 findAll 테스트 (인자가 있는 경우, name 을 통해 일치하는 모든 결과를 가져오도록 한다.)")
    @ParameterizedTest
    @CsvSource(value = {
            "VARARGS, PS, NOARGS"
    })
    public void findAll(ParamType paramType) {
        UserDao userDao = new UserDao();
        List<User> users = null;
        if (paramType == ParamType.VARARGS) {
            users = userDao.findAll_varargs("자바지기");
        } else if (paramType == ParamType.PS) {
            users = userDao.findAll_ps("자바지기");
        } else if (paramType == ParamType.NOARGS) {
            users = userDao.findAll();
        }
        assertThat(users).hasSize(2);
    }
}
