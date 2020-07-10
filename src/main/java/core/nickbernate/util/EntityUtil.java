package core.nickbernate.util;

import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import core.nickbernate.session.EntityKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class EntityUtil {

    private static final Class<Entity> ENTITY_ANNOTATION = Entity.class;
    private static final Class<Id> ENTITY_ID_ANNOTATION = Id.class;

    public static <T> EntityKey createEntityKeyFrom(T entity) {
        Class<?> entityClass = entity.getClass();
        if (entityClass.isAnnotationPresent(ENTITY_ANNOTATION)) {
            return findEntityKey(entity);
        }

        throw new IllegalArgumentException("Entity annotation does not exist.");
    }

    public static Field findEntityIdField(Class<?> entityClass) {
        List<Field> fields = getIdFields(entityClass);
        if (isIdFieldUnique(fields)) {
            return fields.get(0);
        }

        throw new IllegalArgumentException("Entity Id annotation must be unique.");
    }

    public static <T> T createNewInstance(Class<T> entityClass) {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Entity instance creation failed.", e);
        }
    }

    public static <T> List<Field> scanEntityFields(Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    private static <T> EntityKey findEntityKey(T entity) {
        try {
            Field field = findEntityIdField(entity.getClass());
            field.setAccessible(true);

            return new EntityKey(entity.getClass(), field.get(entity));

        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Getting Entity Id is failed.", e);
        }
    }

    private static <T> List<Field> getIdFields(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ENTITY_ID_ANNOTATION))
                .collect(Collectors.toList());
    }

    private static boolean isIdFieldUnique(List<Field> fields) {
        return fields.size() == 1;
    }

}
