package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultPreparedStatementSetterTest extends TestDatabaseSetup {

    private Connection connection;
    private PreparedStatement preparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        super.setup();

        final String sql = "SELECT name FROM USERS WHERE userId = ? and email = ?";
        connection = ConnectionManager.getConnection();
        preparedStatement = connection.prepareStatement(sql);
    }

    @AfterEach
    void tearDown() throws SQLException {
        preparedStatement.close();
        connection.close();
    }

    @DisplayName("preparedStatement 닫혀 있으면 예외가 발생한다")
    @Test
    void prepared_statement_is_closed_an_exception_is_thrown() throws SQLException {
        preparedStatement.close();

        final DefaultPreparedStatementSetter defaultPreparedStatementSetter = new DefaultPreparedStatementSetter("admin", 20);

        assertThatThrownBy(() -> defaultPreparedStatementSetter.setValues(preparedStatement))
            .isInstanceOf(JdbcTemplateException.class)
            .hasMessage("prepared statement is closed");
    }

    @DisplayName("SQL문에 인자 맵핑")
    @Test
    void arguments_mapping() throws SQLException {
        final DefaultPreparedStatementSetter defaultPreparedStatementSetter = new DefaultPreparedStatementSetter("admin", 20);
        defaultPreparedStatementSetter.setValues(preparedStatement);

        final int parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
        final String stringParameterClassName = preparedStatement.getParameterMetaData().getParameterClassName(1);
        final String integerParameterClassName = preparedStatement.getParameterMetaData().getParameterClassName(2);

        assertThat(parameterCount).isEqualTo(2);
        assertThat(stringParameterClassName).isEqualTo(String.class.getName());
        assertThat(integerParameterClassName).isEqualTo(Integer.class.getName());
    }

    @DisplayName("SQL 인자 수와 전달한 인자의 수가 다르면 예외가 발생한다")
    @Test
    void arguments_count_mismatch() {
        final DefaultPreparedStatementSetter defaultPreparedStatementSetter = new DefaultPreparedStatementSetter("admin");

        assertThatThrownBy(() -> defaultPreparedStatementSetter.setValues(preparedStatement))
            .isInstanceOf(JdbcTemplateException.class)
            .hasMessage("인자의 수가 일치하지 않습니다. SQL 인자 수: 2, 입력 인자 수: 1");
    }
}
