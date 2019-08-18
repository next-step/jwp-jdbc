package core.db;

import core.db.jdbc.JdbcTemplate;
import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTemplateTest {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateTest.class);
    private static JdbcTemplate template;

    @BeforeAll
    public static void init() {
        template = new JdbcTemplate();
    }

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("query list")
    @Test
    public void list() throws Exception {
        List<User> users = template.query("SELECT userId, password, name, email FROM USERS", User.class);
        logger.debug("list: {}", users);
        assertFalse(users.isEmpty());
    }

    @DisplayName("query single item")
    @Test
    public void single() throws Exception {
        String findSingleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        Optional<User> user = template.querySingle(findSingleQuery, User.class, "admin");
        logger.debug("user: {}", user);
        assertTrue(user.isPresent());
        assertEquals("admin", user.get().getUserId());

        Optional<User> user2 = template.querySingle(findSingleQuery, User.class, "admin2");
        logger.debug("user: {}", user2);
        assertFalse(user2.isPresent());
    }
}