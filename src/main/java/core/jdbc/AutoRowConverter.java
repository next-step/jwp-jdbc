package core.jdbc;

import core.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AutoRowConverter<T> implements RowConverter<T> {
    private static final Logger logger = LoggerFactory.getLogger(AutoRowConverter.class);

    private final Class<T> clazz;

    public AutoRowConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convert(ResultSet resultSet) {
        return (T) extract(resultSet);
    }

    public Object extract(ResultSet resultSet) {
        try {
            Object instance = newInstance();
            setFields(resultSet, instance);
            return instance;
        } catch (Exception e) {
            throw new ConvertException("Fail to create instance : " + clazz);
        }
    }

    private void setFields(ResultSet resultSet, Object instance) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> setPrivateFieldValue(instance, field, resultSet));
    }

    // 새 인스턴스 생성
    private Object newInstance() {
        try {
            Constructor<T> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException("Fail to create new instance of " + clazz.getName());
        }
    }

    // 객체의 private 필드에 값 셋팅
    private void setPrivateFieldValue(Object instance, Field field, ResultSet resultSet) {
        try {
            String column = resultSet.getString(field.getName());

            field.setAccessible(true);
            field.set(instance, ReflectionUtils.convertStringValue(column, field.getType()));
        } catch (IllegalAccessException | SQLException e) {
            logger.debug("Fail to set value at private field [{}]", e.getMessage());
        }
    }
}
