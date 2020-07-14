package core.jdbc;

import next.exception.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JdbcTemplateTest {

    @Test
    public void exceptionTest() {
        String sql = "SELECT * FROM a";
        assertThatThrownBy(() -> {
            JdbcTemplate.executeQuery(sql, null);
        }).isInstanceOf(DataAccessException.class);
    }

}
