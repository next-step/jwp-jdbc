package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.entitymanager.DefaultEntityManagerFactory;
import core.jdbc.entitymanager.EntityManager;
import core.jdbc.entitymanager.EntityManagerFactory;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityManagerTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void entityManagerPersist() {
        DataSource dataSource = ConnectionManager.getDataSource();
        EntityManagerFactory entityManagerFactory = new DefaultEntityManagerFactory(dataSource);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User expected = new User("userId", "password", "name", "javajigi@email.com");

        entityManager.begin();

        entityManager.persist(expected);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        User actual = findUserByNativeQuery(jdbcTemplate);
        assertThat(actual).isNull();

        entityManager.commit();

        User actual2 = findUserByNativeQuery(jdbcTemplate);
        assertThat(actual2).isNotNull();
        assertThat(expected.isSameUser(actual2)).isTrue();

        User actual3 = entityManager.findById(User.class, expected.getUserId());
        assertThat(expected == actual3).isTrue();
    }

    private User findUserByNativeQuery(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql,
                new Object[]{"userId"},
                resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"))
        );
    }

}
