package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.nickbernate.manager.DefaultEntityManagerFactory;
import core.nickbernate.manager.EntityManager;
import core.nickbernate.manager.EntityManagerFactory;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityManagerTest {

    private DataSource dataSource;
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        dataSource = ConnectionManager.getDataSource();
        this.entityManagerFactory = new DefaultEntityManagerFactory(dataSource);
    }

    @DisplayName("Entity 조회하기")
    @Test
    void entityManagerFindById() {
        /* given */
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User expected = new User("testId", "password", "name", "javajigi@email.com");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        insertUser(jdbcTemplate, expected);

        /* when */
        User actual = entityManager.findById(User.class, expected.getUserId());

        /* then */
        assertThat(expected.isSameUser(actual)).isTrue();
    }

    @DisplayName("한번 조회한 Entity는 캐싱하여 관리한다.")
    @Test
    void entityManagerFindByIdCache() {
        /* given */
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User expected = new User("testId", "password", "name", "javajigi@email.com");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        insertUser(jdbcTemplate, expected);

        User user = entityManager.findById(User.class, expected.getUserId());

        /* when */
        User user2 = entityManager.findById(User.class, expected.getUserId());

        /* then */
        assertThat(user.isSameUser(user2)).isTrue();
        assertThat(user == user2).isTrue();
    }

    @Test
    void entityManagerPersist() {
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

    private void insertUser(JdbcTemplate jdbcTemplate, User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        });
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
