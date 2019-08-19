package core.di.factory;

import com.google.common.collect.Maps;
import core.db.jdbc.JdbcTemplate;
import next.dao.LegacyUserDao;
import next.dao.NewUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SingletonFactory {

    private static final Logger logger = LoggerFactory.getLogger(SingletonFactory.class);

    private static Map<Class<?>, Object> beans = Maps.newConcurrentMap();

    static {

        register(new JdbcTemplate());
        register(new LegacyUserDao());
        register(new NewUserDao(getBean(JdbcTemplate.class)));

        logger.info("SingletonFactory initialize.");
        beans.forEach((key, value) -> logger.info("{} = {}", key, value));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public static Object register(Object bean) {
        return beans.put(bean.getClass(), bean);
    }

}
