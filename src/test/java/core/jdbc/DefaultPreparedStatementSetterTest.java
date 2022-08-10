package core.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.PreparedStatement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultPreparedStatementSetterTest {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPreparedStatementSetterTest.class);

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @DisplayName("별도의 인수가 없는 객체를 생성해 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"select * from users", "update users set name = 'newAdmin' where name='admin'"})
    void constructDefaultInstance(String sql) throws Exception {
        PreparedStatementSetter pss = new DefaultPreparedStatementSetter(sql);

        PreparedStatement ps = pss.values();
        String extractQuery = ps.toString().split("prep0: ")[1];
        logger.info("extrcted query: {}", extractQuery);
        assertThat(extractQuery).isEqualTo(sql);
        assertThat(ps.getParameterMetaData().getParameterCount()).isZero();
    }

    @DisplayName("인수를 포함한 객체를 생성해 반환한다.")
    @Test
    void constructWithParameters() throws Exception {
        String sql = "INSERT INTO USERS VALUES(?, ?, ?, ?);";
        List<Object> params = List.of("catsbi", "abc123", "hansol", "catsbi@nextstep.com");
        PreparedStatementSetter pss = new DefaultPreparedStatementSetter(sql, params);

        PreparedStatement ps = pss.values();
        logger.info("extrcted query: {}", ps);

        assertThat(ps.getParameterMetaData().getParameterCount()).isEqualTo(4);
    }

}