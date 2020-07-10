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

    public static String createFieldTargetsQuery(Class<?> entityClass) {
        List<String> fieldNames = scanEntityFieldNames(entityClass);

        return String.join(QUERY_TARGET_DELIMITER, fieldNames);
    }

    public static String createFieldValuesQuery(Object entity) {
        List<String> fieldValues = scanEntityFieldValues(entity);

        return String.join(QUERY_TARGET_DELIMITER, fieldValues);
    }

    public static List<String> scanEntityFieldNames(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    private static List<String> scanEntityFieldValues(Object entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException();
                    }
                })
                .map(value -> String.format("'%s'", value))
                .collect(Collectors.toList());
    }

}
