package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataSourceUtilsTest {

    @DisplayName("열려 있는 Connection 객체를 닫는다")
    @Test
    void release_connection() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();

        DataSourceUtils.releaseConnection(connection);

        assertThat(connection.isClosed()).isTrue();
    }

    @DisplayName("열려 있는 Statement 객체를 닫는다")
    @Test
    void release_statement() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();
        final Statement statement = connection.createStatement();

        DataSourceUtils.releaseStatement(statement);
        DataSourceUtils.releaseConnection(connection);

        assertThat(statement.isClosed()).isTrue();
    }

    @DisplayName("열려 있는 ResultSet 객체를 닫는다")
    @Test
    void release_result_set() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("select 1");

        DataSourceUtils.releaseResultSet(resultSet);
        DataSourceUtils.releaseStatement(statement);
        DataSourceUtils.releaseConnection(connection);

        assertThat(resultSet.isClosed()).isTrue();
    }

    @DisplayName("열려 있는 Connection, Statement, ResultSet 객체를 닫는다")
    @Test
    void release_connection_and_statement_and_result_set() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("select 1");

        DataSourceUtils.release(connection, statement, resultSet);

        assertThat(resultSet.isClosed()).isTrue();
        assertThat(statement.isClosed()).isTrue();
        assertThat(connection.isClosed()).isTrue();
    }

    @DisplayName("열려 있는 Connection, Statement 객체를 닫는다")
    @Test
    void release_connection_and_statement() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();
        final Statement statement = connection.createStatement();

        DataSourceUtils.release(connection, statement);

        assertThat(statement.isClosed()).isTrue();
        assertThat(connection.isClosed()).isTrue();
    }

}
