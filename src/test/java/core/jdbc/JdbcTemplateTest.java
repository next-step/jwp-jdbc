package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author KingCjy
 */
public class JdbcTemplateTest {

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("복수 row 선택 테스트")
    public void queryForListTest() {
        String query = "SELECT * FROM USERS";
        User user1 = new User("admin", "password", "자바지기", "admin@slipp.net");
        User user2 = new User("KingCjy", "as", "KingCjy", "tlssycks@gmail.com");

        List<User> actual = jdbcTemplate.queryForList(query, new Object[]{}, getUserMapper());

        assertThat(actual).isEqualTo(Arrays.asList(user1, user2));
        System.out.println(actual);
    }

    @Test
    @DisplayName("단일 row 선택 테스트")
    public void queryForObjectTest() {
        String query = "SELECT * FROM USERS WHERE userId = ?";
        User expected = new User("KingCjy", "as", "KingCjy", "tlssycks@gmail.com");

        User actual = jdbcTemplate.queryForObject(query, getUserMapper(), "KingCjy");

        assertThat(actual).isEqualTo(expected);
        System.out.println(actual);
    }

    @Test
    @DisplayName("업데이트 쿼리 테스트")
    public void updateTest() {
        String userId = "KingCjy";
        String changeName = "updated";
        String query = "UPDATE USERS SET name = ? WHERE userId = ?";
        String selectQuery = "SELECT * FROM USERS WHERE userId = ?";

        int updatedRow = jdbcTemplate.update(query, new Object[]{changeName, userId});
        User user = jdbcTemplate.queryForObject(selectQuery, getUserMapper(), userId);

        assertThat(updatedRow).isOne();
        assertThat(user.getName()).isEqualTo(changeName);
    }

    @Test
    @DisplayName("잘못된 쿼리 테스트")
    public void failQueryTest() {
        assertThatThrownBy(() -> {
            String query = "SELECT * FROMA USERS";
            List<User> users = jdbcTemplate.queryForList(query, new Object[]{}, getUserMapper());
        }).isInstanceOf(JdbcTemplateException.class);
    }

    private RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
