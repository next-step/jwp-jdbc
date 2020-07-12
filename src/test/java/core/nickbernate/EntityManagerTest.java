package core.nickbernate;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.nickbernate.manager.DefaultEntityManagerFactory;
import core.nickbernate.manager.EntityManager;
import core.nickbernate.manager.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityManagerTest {

    private EntityManagerFactory entityManagerFactory;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp-test.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        DataSource dataSource = ConnectionManager.getDataSource();
        this.entityManagerFactory = new DefaultEntityManagerFactory(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @AfterEach
    void tearDown() {
        deleteAllTestEntity();
    }

    @DisplayName("Entity 조회하기")
    @Test
    void entityManagerFindById() {
        /* given */
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TestEntity expected = new TestEntity("testId", "testPassword", "testName");
        insertTestEntity(expected);

        /* when */
        TestEntity actual = entityManager.findById(TestEntity.class, expected.getId());

        /* then */
        assertThat(expected.isSameTestEntity(actual)).isTrue();
    }

    @DisplayName("한번 조회한 Entity는 캐싱하여 관리한다.")
    @Test
    void entityManagerFindByIdCache() {
        /* given */
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TestEntity expected = new TestEntity("testId", "testPassword", "testName");
        insertTestEntity(expected);

        TestEntity testEntity = entityManager.findById(TestEntity.class, expected.getId());

        /* when */
        TestEntity testEntity2 = entityManager.findById(TestEntity.class, expected.getId());

        /* then */
        assertThat(testEntity.isSameTestEntity(testEntity2)).isTrue();
        assertThat(testEntity == testEntity2).isTrue();
    }

    @DisplayName("Entity persist 할 시 commit 전까지는 쿼리를 날리지 않는다.")
    @Test
    void entityManagerPersist() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TestEntity expected = new TestEntity("testId", "testPassword", "testName");

        // Transaction 시작
        entityManager.begin();

        // Entity persist
        entityManager.persist(expected);

        TestEntity actual = findTestEntityByNativeQuery(expected.getId());
        assertThat(actual).isNull();

        // Transaction Commit
        entityManager.commit();

        TestEntity actual2 = findTestEntityByNativeQuery(expected.getId());
        assertThat(actual2).isNotNull();
        assertThat(expected.isSameTestEntity(actual2)).isTrue();

        TestEntity actual3 = entityManager.findById(TestEntity.class, expected.getId());
        assertThat(expected == actual3).isTrue();
    }

    @DisplayName("Entity update 후 commit 시 dirtyChecking으로 update 쿼리가 자동으로 날아간다.")
    @Test
    void entityManagerDirtyChecking() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TestEntity expected = new TestEntity("testId", "testPassword", "testName");

        // Transaction 시작
        entityManager.begin();

        // Entity persist
        entityManager.persist(expected);

        // Entity update
        expected.update("updatedPassword", "updatedName");

        // Transaction Commit
        entityManager.commit();

        TestEntity actual2 = findTestEntityByNativeQuery(expected.getId());
        assertThat(actual2).isNotNull();
        assertThat(expected.getPassword()).isEqualTo("updatedPassword");
        assertThat(expected.getName()).isEqualTo("updatedName");
    }

    private void insertTestEntity(TestEntity testEntity) {
        String sql = "INSERT INTO tests VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql, new Object[]{
                testEntity.getId(),
                testEntity.getPassword(),
                testEntity.getName()
        });
    }

    private TestEntity findTestEntityByNativeQuery(Object id) {
        String sql = "SELECT id, password, name FROM tests WHERE id=?";
        return this.jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                resultSet -> new TestEntity(resultSet.getString("id"), resultSet.getString("password"),
                        resultSet.getString("name"))
        );
    }

    private void deleteAllTestEntity() {
        String sql = "delete from tests";
        this.jdbcTemplate.update(sql);
    }

}
