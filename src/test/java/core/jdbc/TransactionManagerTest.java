package core.jdbc;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionManagerTest {

    private static final Logger log = LoggerFactory.getLogger(TransactionManagerTest.class);

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("정상적인 트랜잭션이 제대로 커밋 되었는지 테스트 해보았다.")
    @Test
    void transaction_commit() {
        final Service1 s1 = new Service1();
        final Service2 s2 = new Service2();
        TransactionManager.processTransaction(() -> {
            s1.test();
            s2.test();
            return "._.";
        });
        final User user = findUser();
        log.debug("user: {}", user);
        assertThat(user).isNotNull();
    }

    @DisplayName("고의적으로 익셉션을 발생시켜 트랜잭션이 롤백 되었는지 확인해보았다.")
    @Test
    void transaction_rollback() {
        TransactionManager.beginTransaction();
        final Service1 s1 = new Service1();
        final Service2 s2 = new Service2();

        try {
            s1.test();
            if (System.currentTimeMillis() > 0) {
                throw new RuntimeException("잘가");
            }
            s2.test();
            TransactionManager.commit();
        } catch (Throwable t) {
            TransactionManager.rollback();
        }

        final User user = findUser();
        log.debug("user: {}", user);
        assertThat(user).isNull();
    }

    public User findUser() {
        ResultSet rs = null;
        final String sql = "SELECT userId, password, name, email FROM users WHERE userId = 'tester'";
        try (
                final Connection conn = ConnectionManager.getConnection();
                final PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            rs = pstmt.executeQuery();
            if (rs.next()) {
                final User user = new User(rs.getString("userId"), rs.getString("password"),
                        rs.getString("name"), rs.getString("email"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throw new RuntimeException("으어 rs");
                }
            }
        }
        return null;
    }

    public static class Service1 {
        public void test() {
            final Connection conn = TransactionManager.getConnection();
            final String sql = "INSERT INTO users VALUES ('tester', '1234abcd', 'chwon', 'a@b.com')";
            try (final PreparedStatement pstmt = conn.prepareStatement(sql)) {
                final int affectedRows = pstmt.executeUpdate();
                log.debug("affected rows on service1: {}", affectedRows);
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException("servce1 - error");
            }
        }
    }

    public static class Service2 {
        public void test() {
            final Connection conn = TransactionManager.getConnection();
            final String sql = "UPDATE users SET name='wonch' WHERE userId = 'tester'";
            try (final PreparedStatement pstmt = conn.prepareStatement(sql)) {
                final int affectedRows = pstmt.executeUpdate();
                log.debug("affected rows on service2: {}", affectedRows);
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException("servce1 - error");
            }
        }
    }
}
