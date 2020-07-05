package core.jdbc.custom;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultUserRepositoryTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void prepareSetQuestionMark() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5 - 1; i++) {
            stringBuilder.append("?, ");
        }
        stringBuilder.append("?");
    }

    @Test
    void fieldGet() throws IllegalAccessException {
        User user = new User("1", "2", "3", "4");
        final Field[] fields = user.getClass()
                .getDeclaredFields();

        fields[0].setAccessible(true);
        assertThat(user.getUserId()).isEqualTo(fields[0].get(user));
    }

    @Test
    void create() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        defaultUserRepository.save(expected);
    }
}
