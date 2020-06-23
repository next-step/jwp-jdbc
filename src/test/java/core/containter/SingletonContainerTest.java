package core.containter;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("싱글턴 컨테이너")
class SingletonContainerTest {
    @BeforeAll
    static void init() {
        SingletonContainer.addSingletons(Collections.singleton(
                new JdbcTemplate(ConnectionManager.getDataSource())
        ));
    }

    @Test
    @DisplayName("같은 객체를 리턴하는지")
    void getInstance() {
        JdbcTemplate one = SingletonContainer.getInstance(JdbcTemplate.class);
        JdbcTemplate another = SingletonContainer.getInstance(JdbcTemplate.class);

        assertThat(one).isNotNull();
        assertThat(one).isEqualTo(another);
    }
}