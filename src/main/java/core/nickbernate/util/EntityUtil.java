package core.nickbernate.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nickbernate.annotation.Entity;
import core.nickbernate.annotation.Id;
import core.nickbernate.persistence.EntityKey;
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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    public static Object getIdFieldValue(Object entity) {
        try {
            Field entityIdField = findEntityIdField(entity.getClass());
            entityIdField.setAccessible(true);

            return entityIdField.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Getting Entity Id field is failed.", e);
        }
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

    public static boolean isAllSameEntityFieldValues(Object entity, Object snapShot) {
        try {
            Field[] snapShotFields = snapShot.getClass().getDeclaredFields();
            for (Field snapShotField : snapShotFields) {
                Field entityField = entity.getClass().getDeclaredField(snapShotField.getName());

                snapShotField.setAccessible(true);
                entityField.setAccessible(true);

                Object snapShotFieldValue = snapShotField.get(snapShot);
                Object entityFieldValue = entityField.get(entity);

                if (!snapShotFieldValue.equals(entityFieldValue)) {
                    return false;
                }
            }

            return true;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Entity Field Comparing Failed.", e);
        }
    }

    public static Object copyFromEntity(Object entity) {
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(entity), entity.getClass());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Entity copy failed.", e);
        }
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
