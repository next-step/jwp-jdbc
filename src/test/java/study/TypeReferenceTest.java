package study;

import org.junit.jupiter.api.Test;
import org.springframework.asm.TypeReference;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypeReferenceTest {

    @Test
    void typeCheck() {
        ParameterizedTypeReference<User> userType = new ParameterizedTypeReference<User>() {};
        assertThat(userType.getType()).isEqualTo(User.class);

        ParameterizedTypeReference<List<User>> listType = new ParameterizedTypeReference<List<User>>() {};
        ParameterizedType parameterizedType = (ParameterizedType) listType.getType();
        assertThat(parameterizedType.getRawType()).isEqualTo(List.class);
        assertThat(parameterizedType.getActualTypeArguments()[0]).isEqualTo(User.class);
    }

    private static class User {
        private String id;
    }

    @Test
    void newInstance() {
        ClientFactory<Client> factory = new ClientFactory<>();
        assertThrows(InstantiationException.class, factory::getClient);
    }

    private static class Client {}

    private static class ClientFactory<T> extends ParameterizedTypeReference<T> {
        public T getClient() throws IllegalAccessException, InstantiationException {
            assertThat(getType().getTypeName()).isEqualTo("T");
            return (T) getType().getClass().newInstance();
        }
    }

}
