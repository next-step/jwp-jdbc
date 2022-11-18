package core.jdbc;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RowMapperImpl<T> implements RowMapper {
    private static final LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final Class<T> objectType;

    public RowMapperImpl(Class<T> objectType) {
        this.objectType = objectType;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        Constructor<T> targetConstructor = null;

        Constructor<?>[] constructors = objectType.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();

            if (!isEqualParameterNames(rs, parameterTypes)) {
                throw new IllegalStateException("인스턴스 생성 불가 (인스턴스 변수이름과 테이블 칼럼 이름이 맞지 않습니다)");
            }

            targetConstructor = constructor;
        }

        try {
            return newInstance(targetConstructor, rs);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isEqualParameterNames(ResultSet rs, Class[] parameterTypes) throws SQLException {
        for (int i = 0; i < parameterTypes.length; ++i) {
            Field[] declaredFields = objectType.getDeclaredFields();
            ResultSetMetaData metaData = rs.getMetaData();

            String columnName = metaData.getColumnName(i + 1).toUpperCase();
            String parameterName = declaredFields[i].getName().toUpperCase();

            if (!columnName.equals(parameterName)) {
                return false;
            }
        }

        return true;
    }

    private T newInstance(Constructor<T> targetConstructor, ResultSet rs) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String[] parameterNames = nameDiscoverer.getParameterNames(targetConstructor);

        Object[] args = new Object[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++) {
            args[i] = rs.getObject(parameterNames[i]);
        }
        return targetConstructor.newInstance(args);
    }
}
