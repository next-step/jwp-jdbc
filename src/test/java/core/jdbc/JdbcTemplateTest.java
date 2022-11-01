package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("Insert 테스트")
    @Test
    void Insert_테스트() {
        User expected = new User("aaa", "aaaa", "aaaaa", "aa@naver.com");
        JdbcTemplate.getInstance()
                .update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail());

        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", getResultSetByUsers(), expected.getUserId());

        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("Update 테스트")
    @Test
    void Update_테스트() {
        final String newPasswd = "bbb";
        final String newName = "bbbb";
        final String newEmail = "bbbb@naver.com";

        User user = new User("aaa", "aaaa", "aaaaa", "aa@naver.com");
        JdbcTemplate.getInstance()
                .update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        JdbcTemplate.getInstance()
                .update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", newPasswd, newName, newEmail, user.getUserId());

        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", getResultSetByUsers(), user.getUserId());

        assertThat(actual.getPassword()).isEqualTo(newPasswd);
        assertThat(actual.getName()).isEqualTo(newName);
        assertThat(actual.getEmail()).isEqualTo(newEmail);
    }

    @DisplayName("Delete 테스트")
    @Test
    void Delete_테스트() {
        User user = new User("aaa", "aaaa", "aaaaa", "aa@naver.com");
        JdbcTemplate.getInstance()
                .update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        JdbcTemplate.getInstance()
                .update("DELETE FROM USERS WHERE userId = ?", user.getUserId());

        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", getResultSetByUsers(), user.getUserId());

        assertThat(actual).isNull();
    }

    @DisplayName("List 조회 검증")
    @Test
    void List_query_테스트() {
        User expected1 = new User("a", "aa", "aaa", "aaaa@naver.com");
        User expected2 = new User("b", "bb", "bbb", "bbbb@naver.com");
        User expected3 = new User("c", "cc", "ccc", "cccc@naver.com");

        JdbcTemplate.getInstance().update(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)," +
                        "(?, ?, ?, ?)," +
                        "(?, ?, ?, ?)", expected1.getUserId(), expected1.getPassword(), expected1.getName(), expected1.getEmail(),
                expected2.getUserId(), expected2.getPassword(), expected2.getName(), expected2.getEmail(),
                expected3.getUserId(), expected3.getPassword(), expected3.getName(), expected3.getEmail());

        List<User> expected = JdbcTemplate.getInstance()
                .query("SELECT * FROM USERS", getResultSetByUsers())
                .stream()
//                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());

        assertThat(expected.size()).isEqualTo(4);
        assertThat(expected).contains(expected1, expected2, expected3);
    }

    private RowMapper<User> getResultSetByUsers() {
        return resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }
}
