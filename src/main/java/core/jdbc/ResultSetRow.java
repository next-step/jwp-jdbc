package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static core.util.ReflectionUtil.getFieldBySetMethodName;
import static java.util.stream.Collectors.toList;

/**
 * populate object T with ResultSet columns.
 * if class does not have setter method, does not populate it
 * @param <T> target Object
 */
public class ResultSetRow<T> {

    private static final Logger logger = LoggerFactory.getLogger(ResultSetRow.class);

    private List<ResultSetMapping> rsMappings;

    public ResultSetRow(Class<T> clazz) {
        this.rsMappings = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> method.getParameterCount() == 1)
                .map(ResultSetMapping::create)
                .collect(toList());
    }

    public void populateRow(T row, ResultSet resultSet) throws SQLException {
        try {
            doPopulateRow(row, resultSet);
        } catch (InvocationTargetException e) {
            logger.error(row.getClass() + " invoking method failed", e);
        } catch (IllegalAccessException e) {
            logger.error(row.getClass() + " access method failed", e);
        }
    }

    private void doPopulateRow(T row, ResultSet resultSet) throws SQLException, InvocationTargetException, IllegalAccessException {
        for (ResultSetMapping rsMapping : rsMappings) {
            final Object object = resultSet.getObject(rsMapping.getFieldName(), rsMapping.getType());
            rsMapping.getMethod().invoke(row, rsMapping.getType().cast(object));
        }
    }

    private static class ResultSetMapping {
        private Method method;
        private String fieldName;
        private Class<?> type;

        private ResultSetMapping() {}

        public static ResultSetMapping create(Method method) {
            ResultSetMapping rsMapping = new ResultSetMapping();
            rsMapping.setMethod(method);
            rsMapping.setFieldName(getFieldBySetMethodName(method.getName()));
            rsMapping.setType(method.getParameterTypes()[0]);
            return rsMapping;
        }

        public Method getMethod() {
            return method;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getType() {
            return type;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }
    }

}
