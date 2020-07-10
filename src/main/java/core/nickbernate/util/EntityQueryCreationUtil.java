package core.nickbernate.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class EntityQueryCreationUtil {

    private static final String QUERY_TARGET_DELIMITER = ", ";

    public static String createFieldTargetQuery(Class<?> entityClass) {
        List<String> fieldNames = scanEntityFieldNames(entityClass);

        return String.join(QUERY_TARGET_DELIMITER, fieldNames);
    }

    public static <T> List<String> scanEntityFieldNames(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

}
