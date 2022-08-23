package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import core.jdbc.exception.JdbcTemplateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PreparedStatementTypeSetterTest extends TestDatabaseSetup {

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

    @DisplayName("PreparedStatement에 전달할 수 없는 인자 타입을 전달하면 예외가 발생한다")
    @Test
    void unsupported_parameter_type() {
        double parameter = 0.0d;

        final ThrowingCallable actual = () -> PreparedStatementTypeSetter.setValue(preparedStatement, 1, parameter);

        assertThatThrownBy(actual).isInstanceOf(JdbcTemplateException.class)
            .hasMessage("지원하지 않는 타입의 값입니다. 타입: class java.lang.Double, 값: 0.0");
    }

    @DisplayName("PreparedStatement에 인자를 전달하여 맵핑한다")
    @Test
    void set_value() throws SQLException {
        String userId = "admin";
        String email = "email@email.com";

        PreparedStatementTypeSetter.setValue(preparedStatement, 1, userId);
        PreparedStatementTypeSetter.setValue(preparedStatement, 2, email);

        final int parameterCount = preparedStatement.getParameterMetaData().getParameterCount();
        final String userIdParameterClassName = preparedStatement.getParameterMetaData().getParameterClassName(1);
        final String emailParameterClassName = preparedStatement.getParameterMetaData().getParameterClassName(2);

        assertThat(parameterCount).isEqualTo(2);
        assertThat(userIdParameterClassName).isEqualTo(String.class.getName());
        assertThat(emailParameterClassName).isEqualTo(String.class.getName());
    }
}
