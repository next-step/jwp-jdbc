package core.mvc.tobe;

import next.dao.NewUserDao;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonRegistry {

    private final static Map<String, Object> singletons = new ConcurrentHashMap<>();

    static {
        singletons.put("userDao", new NewUserDao());
    }

    public static Optional<Object> getSingleton(String id) {
        return Optional.ofNullable(singletons.get(id));
    }

}
