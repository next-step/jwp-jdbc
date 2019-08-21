package core.mvc.tobe;

import java.util.HashMap;
import java.util.Map;

public class BeanRegistry {
    private static final Map<Class<?>, Object> typeBeans = new HashMap<>();

    public static <T> void addBean(Class<T> key, T obj) {
        typeBeans.put(key, obj);
    }

    public static <T> T getBean(Class<T> key) {
        return (T) typeBeans.get(key);
    }
}
