package core.jdbc;

import next.exception.QueryExecuteException;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created By kjs4395 on 2020-07-09
 */
public class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate;
    private String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private String selectAllSql = "SELECT userId, password, name, email FROM USERS";
    private String selectSql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";


    @BeforeEach
    void setUp() throws InterruptedException {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate();
    }

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        List beforeResults = jdbcTemplate.findAll(selectAllSql, rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email")));
        jdbcTemplate.insertOrUpdate(insertSql, "kjs4395","1234","지서니","kjs4395@gamil.com");

        List afterResults = jdbcTemplate.findAll(selectAllSql, rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email")));

        assertEquals(beforeResults.size()+1, afterResults.size());
    }

    @Test
    @DisplayName("insert 할때 주어진 조건 부터 더 많은 조건을 주면 에러")
    void insertArgumentManyThrow() {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        assertThrows(QueryExecuteException.class, () ->jdbcTemplate.insertOrUpdate(sql, "kjs4395","1234","지서니","kjs4395@gamil.com","test"));
    }

    @Test
    void selectOneTest() {
        User user = (User) jdbcTemplate.findById(selectSql, rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email")), "admin");

        assertEquals("자바지기", user.getName());
    }

    @Test
    void selectAllTest() {
        jdbcTemplate.insertOrUpdate(insertSql, "kjs4395","1234","지서니","kjs4395@gamil.com");
        List userList = jdbcTemplate.findAll(selectAllSql, rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email")));

        assertTrue(!userList.isEmpty());

    }
}
