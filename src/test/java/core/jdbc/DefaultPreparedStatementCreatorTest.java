package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

class DefaultPreparedStatementCreatorTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("사용 가능한 PreparedStatement 객체 생성")
    @Test
    void not_closed() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM USERS WHERE name = ?";
        final PreparedStatementCreator creator = new DefaultPreparedStatementCreator(sql, "admin");
        final PreparedStatement preparedStatement = creator.createPreparedStatement(ConnectionManager.getConnection());

        final boolean actual = preparedStatement.isClosed();

        assertThat(actual).isFalse();
    }

    @DisplayName("SQL문에 인자 맵핑")
    @Test
    void arguments_mapping() throws SQLException {
        final String sql = "SELECT name FROM USERS WHERE userId = ? and password = ?";
        final PreparedStatementCreator creator = new DefaultPreparedStatementCreator(sql, "admin", "passsword");
        final PreparedStatement preparedStatement = creator.createPreparedStatement(ConnectionManager.getConnection());

        final int actual = preparedStatement.getParameterMetaData().getParameterCount();

        assertThat(actual).isEqualTo(2);
    }

    @DisplayName("SQL 인자 수와 전달한 인자의 수가 다르면 예외가 발생한다")
    @Test
    void arguments_count_mismatch() {
        final String sql = "SELECT name FROM USERS WHERE userId = ? and password = ?";
        final PreparedStatementCreator creator = new DefaultPreparedStatementCreator(sql, "admin");
        final Connection connection = ConnectionManager.getConnection();

        assertThatThrownBy(() -> creator.createPreparedStatement(connection))
            .isInstanceOf(JdbcTemplateException.class)
            .hasMessage("인자의 수가 일치하지 않습니다. SQL 인자 수: 2, 입력 인자 수: 1");
    }

}
