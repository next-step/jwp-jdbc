package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = ConnectionManager.getConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @DisplayName("트랜잭션 객체를 생성하면 커넥션의 autoCommit 의 기본 값은 false 로 설정된다")
    @Test
    void default_auto_commit_is_false_on_transaction() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();

        new Transaction(connection);

        assertThat(connection.getAutoCommit()).isFalse();
    }

    @DisplayName("커밋한 경우 롤백할 수 없다")
    @Test
    void cannot_rollback_if_committed() throws SQLException {
        final Transaction transaction = new Transaction(connection);
        transaction.commit();

        final boolean actual = transaction.canRollback();

        assertThat(actual).isFalse();
    }

    @DisplayName("커밋 하지 않은 경우 롤백할 수 있다")
    @Test
    void can_rollback_if_not_committed() throws SQLException {
        final Connection connection = ConnectionManager.getConnection();
        final Transaction transaction = new Transaction(connection);

        final boolean actual = transaction.canRollback();

        assertThat(actual).isTrue();
    }

}
