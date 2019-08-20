package core.db;

import core.db.jdbc.JdbcTemplate;
import core.db.jdbc.ResultTypeRowMapper;
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

    @DisplayName("[Mapper] 리스트 쿼리")
    @Test
    public void list() throws Exception {

        // given
        String listQuery = "SELECT userId, password, name, email FROM USERS";

        // when
        List<User> users = template.query(
                listQuery,
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));

        // then
        logger.debug("list: {}", users);
        assertFalse(users.isEmpty());
    }

    @DisplayName("[Mapper] 단일 쿼리")
    @Test
    public void single() throws Exception {

        // given
        String singleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        String expectedUserId = "admin";

        // when
        Optional<User> user = template.querySingle(
                singleQuery,
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ), expectedUserId);


        // then
        logger.debug("user: {}", user);
        assertTrue(user.isPresent());
    }

    @DisplayName("[Mapper] 단일 쿼리 null 반환")
    @Test
    public void returnNull() throws Exception {
        // given
        String singleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        String expectedUserId = "unknown";

        // when
        Optional<User> user = template.querySingle(
                singleQuery,
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ), expectedUserId);


        // then
        logger.debug("user: {}", user);
        assertFalse(user.isPresent());
    }

    @DisplayName("[resultType] 리스트 쿼리")
    @Test
    public void listWithClass() throws Exception {

        // given
        String listQuery = "SELECT userId, password, name, email FROM USERS";

        // when
        List<User> users = template.query(listQuery, new ResultTypeRowMapper<>(User.class));

        // then
        logger.debug("list: {}", users);
        assertFalse(users.isEmpty());
    }

    @DisplayName("[resultType] 단일 쿼리")
    @Test
    public void singleWithClass() throws Exception {

        // given
        String singleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        String expectedUserId = "admin";

        // when
        Optional<User> user = template.querySingle(singleQuery, new ResultTypeRowMapper<>(User.class), expectedUserId);

        // then
        logger.debug("user: {}", user);
        assertTrue(user.isPresent());
    }

    @DisplayName("[resultType] 단일 쿼리 null 반환")
    @Test
    public void returnNullWithClass() throws Exception {

        // given
        String singleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        String expectedUserId = "unknown";

        // when
        Optional<User> user = template.querySingle(singleQuery, new ResultTypeRowMapper<>(User.class), expectedUserId);

        // then
        logger.debug("user: {}", user);
        assertFalse(user.isPresent());
    }

    @DisplayName("[setter] insert 쿼리")
    @Test
    public void insertWithSetter() throws Exception {

        // given
        String insertQuery = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        User user = new User("ppeltny", "123", "yusik", "ppeltny@git");

        // when
        int result = template.update(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                (pstmt) -> {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, user.getPassword());
                    pstmt.setString(3, user.getName());
                    pstmt.setString(4, user.getEmail());
                }
        );

        // then
        logger.debug("user: {}", user);
        assertEquals(1, result);
    }

    @DisplayName("[setter] update 쿼리")
    @Test
    public void updateWithSetter() throws Exception {

        // given
        String updateQuery = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        User user = new User("admin", "123", "yusik", "ppeltny@git");

        // when
        int result = template.update(
                updateQuery,
                (pstmt) -> {
                    pstmt.setString(1, user.getPassword());
                    pstmt.setString(2, user.getName());
                    pstmt.setString(3, user.getEmail());
                    pstmt.setString(4, user.getUserId());
                }
        );

        // then
        logger.debug("user: {}", user);
        assertEquals(1, result);
    }

    @DisplayName("[setter/mapper] select 단일 쿼리")
    @Test
    public void selectWithSetterMapper() throws Exception {

        // given
        String singleQuery = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        String expectedUserId = "admin";

        // when
        Optional<User> user = template.querySingle(
                singleQuery,
                rs -> new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ),
                pstmt -> pstmt.setString(1, expectedUserId));

        // then
        logger.debug("user: {}", user);
        assertTrue(user.isPresent());
    }
}