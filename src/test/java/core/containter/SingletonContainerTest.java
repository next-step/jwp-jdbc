package core.containter;

import core.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("싱글턴 컨테이너")
class SingletonContainerTest {

    @Test
    @DisplayName("같은 객체를 리턴하는지")
    void getInstance() {
        JdbcTemplate one = SingletonContainer.getInstance(JdbcTemplate.class);
        JdbcTemplate another = SingletonContainer.getInstance(JdbcTemplate.class);

        assertThat(one).isEqualTo(another);
    }
}