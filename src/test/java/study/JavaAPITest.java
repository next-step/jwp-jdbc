package study;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JavaAPITest {

    @Test
    void findMapNull() {
        Map<String, String> map = new HashMap<>();
        String result = map.get(null);
        assertThat(result).isEqualTo(null);
    }

    @Test
    void optional_nullable() {
        Map<String, String> map = new HashMap<>();
        Optional<String> nullValue = Optional.ofNullable(map.get("null"));
        assertFalse(nullValue.isPresent());
        assertThat(nullValue.orElse("else")).isEqualTo("else");
    }

    @Test
    void reflectionTypeMethod() {
        Client client = new Client();
        client.setSuperOne(new SubOne());

        try {
            final Method setSuperOne = client.getClass().getMethod("setSuperOne", SuperOne.class);
            System.out.println(setSuperOne.getName());
            assertNotNull(setSuperOne.getName());
        } catch (NoSuchMethodException ignored) {
            fail();
        }
    }

    @Test
    void reflectionSuperTypeMethod() {
        Client client = new Client();
        try {
            client.getClass().getMethod("setSuperOne", SubOne.class);
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
            return;
        }
        fail();
    }

    @Test
    void reflectionTypeMethodUpdated() {
        Client client = new Client();
        try {
//            client.getClass().getMethod("setSuperOne", SubOne.class);
            client.getClass().getDeclaredMethod("setSuperOne", SubOne.class);
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
            return;
        }
        fail();
    }

    public static class Client {
        private SuperOne superOne;

        public void setSuperOne(SuperOne superOne) {
            this.superOne = superOne;
        }
    }

    public static class SubOne extends SuperOne {

    }

    public static class SuperOne {

    }

}
