package core.jdbc.custom;

import core.jdbc.ConnectionManager;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void prepareUpdateQueryTest() {
        List<String> mark = new ArrayList<>();
        final Field[] fields = User.class.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            mark.add(String.format("%s = ?", fields[i].getName()));
        }
        assertThat(String.join(",", mark)).isEqualTo("password = ?,name = ?,email = ?");
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
    void crud() {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        defaultUserRepository.save(expected);

        User actual = defaultUserRepository.findById("userId");
        assertThat(actual).isEqualTo(expected);

        expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
        defaultUserRepository.save(expected);

        actual = defaultUserRepository.findById("userId");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAll() {
        DefaultUserRepository defaultUserRepository = new DefaultUserRepository();
        List<User> users = defaultUserRepository.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    void name() {
        String s = "thisIsMyString";
        String[] r = s.split("(?=\\p{Upper})");
        System.out.println(Arrays.toString(r));
    }
}
