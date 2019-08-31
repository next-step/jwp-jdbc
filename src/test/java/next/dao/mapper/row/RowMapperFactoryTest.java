package next.dao.mapper.row;

import next.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RowMapperFactoryTest {
    @Test
    void create() {
        RowMapper<User> rowMapper = RowMapperFactory.newInstance(User.class, "userId");
        assertThat(rowMapper).isNotNull();
    }
}