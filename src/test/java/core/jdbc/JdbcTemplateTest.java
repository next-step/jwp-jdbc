package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Created by iltaek on 2020/07/12 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("PreparedStatementSetter로 update, id로 query 수행 테스트")
    @Test
    void updateWithPSSTest() {
        User expected = new User("id1234", "qwer", "green", "green@green.com");
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setObject(1, expected.getUserId());
            pstmt.setObject(2, expected.getPassword());
            pstmt.setObject(3, expected.getName());
            pstmt.setObject(4, expected.getEmail());
        };
        JdbcTemplate.update(insertSql, pss);

        User actual = findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    private User findByUserId(String userId) {
        String querySql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return JdbcTemplate.queryForObject(querySql,
                                           rs -> new User(rs.getString("userId"),
                                                          rs.getString("password"),
                                                          rs.getString("name"),
                                                          rs.getString("email")),
                                           userId);
    }

    @DisplayName("Params로 update, id로 query 수행 테스트")
    @Test
    void updateWithPramsTest() {
        User expected = new User("id1234", "qwer", "green", "green@green.com");
        insertUsers(expected);

        User actual = findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    private void insertUsers(User... users) {
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Arrays.stream(users)
              .forEach(user -> JdbcTemplate.update(insertSql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    @DisplayName("queryForList 수행 테스트")
    @Test
    void findAllTest() {
        User expected1 = new User("id1234", "qwer", "green", "green@green.com");
        User expected2 = new User("id9999", "rewq", "red", "red@red.com");
        insertUsers(expected1, expected2);

        String findAllQuery = "SELECT userId, password, name, email FROM USERS";
        List<User> actualUsers = JdbcTemplate.queryForList(findAllQuery,
                                                           rs -> new User(rs.getString("userId"),
                                                                          rs.getString("password"),
                                                                          rs.getString("name"),
                                                                          rs.getString("email")));
        assertThat(actualUsers.size()).isEqualTo(3);
        assertThat(actualUsers).contains(expected1, expected2);
    }
}