package core.jdbc;

import next.exception.DataAccessException;
import next.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcTemplateTest {

    @Test
    public void exceptionTest() {
        String sql = "SELECT * FROM a";
        assertThatThrownBy(() -> {
            JdbcTemplate.executeQuery(sql, User.class);
        }).isInstanceOf(DataAccessException.class);
    }

}
