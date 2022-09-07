package core.jdbc;

import next.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.stream.Collectors;

public class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("insert & queryForObject 검증")
    @Test
    void insertAndqueryForObjectTest() {
        User expected = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail());

        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", this.rowMapper(), expected.getUserId());

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("update & queryForObject 검증")
    @Test
    void updateAndqueryForObjectTest() {
        User user = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        JdbcTemplate.getInstance().update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", "password2", "name2", "kimkim@gmail.com", user.getUserId());
        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", this.rowMapper(), user.getUserId());

        Assertions.assertThat(actual.getPassword()).isEqualTo("password2");
        Assertions.assertThat(actual.getName()).isEqualTo("name2");
        Assertions.assertThat(actual.getEmail()).isEqualTo("kimkim@gmail.com");
    }

    @DisplayName("delete & queryForObject 검증")
    @Test
    void deleteAndqueryForObjectTest() {
        User user = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        JdbcTemplate.getInstance().update("DELETE FROM USERS WHERE userId = ?", user.getUserId());
        User actual = JdbcTemplate.getInstance()
                .queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId = ?", this.rowMapper(), user.getUserId());

        Assertions.assertThat(actual).isNull();
    }


    @DisplayName("리스트 조회 검증")
    @Test
    void query() {
        User expected1 = new User("userId1", "password", "name1", "fistkim101@email.com");
        User expected2 = new User("userId2", "password", "name2", "fistkim101@email.com");
        User expected3 = new User("userId3", "password", "name3", "fistkim101@email.com");

        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected1.getUserId(), expected1.getPassword(), expected1.getName(), expected1.getEmail());
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected2.getUserId(), expected2.getPassword(), expected2.getName(), expected2.getEmail());
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected3.getUserId(), expected3.getPassword(), expected3.getName(), expected3.getEmail());

        List<User> expected = JdbcTemplate.getInstance()
                .query("SELECT * FROM USERS", this.rowMapper())
                .stream()
                .map(resultObject -> (User) resultObject)
                .collect(Collectors.toList());

        Assertions.assertThat(expected.size()).isEqualTo(3);
    }

    @DisplayName("잘못된 parameter 에러 검증")
    @Test
    void wrongParameterTest() {
        User expected = new User("userId", "password", "name", "fistkim101@email.com");

        Assertions.assertThatThrownBy(() -> JdbcTemplate.getInstance()
                        .update("INSERT INTO USERS VALUES (?, ?, ?, ?)", expected.getEmail()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("잘못된 insert sql 문 에러 검증")
    @Test
    void wrongInsertQueryTest() {
        User expected = new User("userId", "password", "name", "fistkim101@email.com");

        Assertions.assertThatThrownBy(() -> JdbcTemplate.getInstance()
                        .update("INT INTO USERS VALUES (?, ?, ?, ?)", expected.getUserId(), expected.getPassword(), expected.getName(), expected.getEmail()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("잘못된 update sql 문 에러 검증")
    @Test
    void wrongUpdateQueryTest() {
        User user = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        Assertions.assertThatThrownBy(() -> JdbcTemplate.getInstance()
                        .update("UE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", "password2", "name2", "kimkim@gmail.com", user.getUserId()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("잘못된 delete sql 문 에러 검증")
    @Test
    void wrongDeleteQueryTest() {
        User user = new User("userId", "password", "name", "fistkim101@email.com");
        JdbcTemplate.getInstance().update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        Assertions.assertThatThrownBy(() -> JdbcTemplate.getInstance()
                        .update("DTE FROM USERS WHERE userId = ?", user.getUserId()))
                .isInstanceOf(RuntimeException.class);
    }

    private RowMapper<User> rowMapper() {
        return resultSet -> new User(
                resultSet.getString("userId"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("email"));
    }

}
