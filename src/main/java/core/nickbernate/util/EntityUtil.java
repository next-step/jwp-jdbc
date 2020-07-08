package core.nickbernate.util;

import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityUtil {

    private static final Class<Entity> ENTITY_ANNOTATION = Entity.class;
    private static final Class<Id> ENTITY_ID_ANNOTATION = Id.class;

    public static <T> Object findIdFrom(T entity) {
        Class<?> entityClass = entity.getClass();
        if (entityClass.isAnnotationPresent(ENTITY_ANNOTATION)) {
            return findEntityId(entity);
        }

        throw new IllegalArgumentException("Entity annotation does not exist.");
    }

    private static <T> Object findEntityId(T entity) {
        try {
            List<Field> fields = getIdFields(entity);

            if (isIdFieldUnique(fields)) {
                Field field = fields.get(0);
                field.setAccessible(true);

                return field.get(entity);
            }

            throw new IllegalArgumentException("Entity Id annotation must be unique.");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Getting Entity Id is failed.", e);
        }
    }

    private static <T> List<Field> getIdFields(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ENTITY_ID_ANNOTATION))
                .collect(Collectors.toList());
    }

    private static boolean isIdFieldUnique(List<Field> fields) {
        return fields.size() == 1;
    }

}
