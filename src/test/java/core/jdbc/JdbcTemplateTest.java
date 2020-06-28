package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate;
    private User user = new User("parkeeseul", "test", "seul", "parkeeseul@nextstep.com");

    @BeforeEach
    void setUp() throws InterruptedException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate();
    }

    @Test
    void update_preparedStatementSetter() {
        String sql = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, user.getUserId());
            preparedStatement.setObject(2, user.getPassword());
            preparedStatement.setObject(3, user.getName());
            preparedStatement.setObject(4, user.getEmail());
        });

        String sql2 = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        User findUser = jdbcTemplate.queryForObject(sql2, rowMapper, new Object[]{user.getUserId()});
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void update() {
        String sql = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});
    }

    @Test
    void queryForObject_preparedStatementSetter() {
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> preparedStatement.setObject(1, user.getUserId());
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        User findUser = jdbcTemplate.queryForObject(sql, rowMapper, preparedStatementSetter);

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void queryForObject() {
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        User findUser = jdbcTemplate.queryForObject(sql, rowMapper, new Object[]{user.getUserId()});

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void queryForList() {
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        List<User> findAllUser = jdbcTemplate.query(sql, rowMapper, new Object[]{user.getUserId()});

        assertThat(findAllUser).hasSize(1);
        assertThat(findAllUser.get(0)).isEqualTo(user);
    }

    @Test
    void transaction_test() {
        jdbcTemplate.beginTransaction();
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});
        jdbcTemplate.commit();

        JdbcTemplate jdbcTemplate2 = new JdbcTemplate();
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        List<User> findAllUser = jdbcTemplate2.query(sql, rowMapper, new Object[]{user.getUserId()});

        assertThat(findAllUser).hasSize(1);
        assertThat(findAllUser.get(0)).isEqualTo(user);
    }

    @Test
    void transaction_rollback_test() {
        jdbcTemplate.beginTransaction();
        String updateQuery = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(updateQuery, new Object[]{user.getUserId(), user.getPassword(), user.getName(), user.getEmail()});
        jdbcTemplate.rollback();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
        List<User> findAllUser = jdbcTemplate.query(sql, rowMapper, new Object[]{user.getUserId()});

        assertThat(findAllUser).hasSize(0);
    }
}
