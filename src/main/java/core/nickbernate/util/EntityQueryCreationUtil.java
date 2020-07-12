package core.nickbernate.util;

import core.nickbernate.annotation.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return findNonStaticFields(entityClass)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    public static String createUpdateFieldsQuery(Object entity) {
        List<String> updateQueries = scanUpdateQueries(entity);

        return String.join(QUERY_TARGET_DELIMITER, updateQueries);
    }

    private static List<String> scanEntityFieldValues(Object entity) {
        return findNonStaticFields(entity.getClass())
                .map(field -> getFieldValue(field, entity))
                .map(value -> String.format("'%s'", value))
                .collect(Collectors.toList());
    }

    private static List<String> scanUpdateQueries(Object entity) {
        return findNonStaticFields(entity.getClass())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .map(field -> {
                    Object fieldValue = getFieldValue(field, entity);

                    return String.format("%s='%s'", field.getName(), fieldValue);
                })
                .collect(Collectors.toList());
    }

    private static Object getFieldValue(Field field, Object instance) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException();
        }
    }

    private static Stream<Field> findNonStaticFields(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()));
    }

}
