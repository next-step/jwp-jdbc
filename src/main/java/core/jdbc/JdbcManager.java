package core.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcManager {
    private static final String BIND_VARIABLE_REGEX = "[#{](.*?)[}]";
    private static final Pattern pattern = Pattern.compile(BIND_VARIABLE_REGEX);
    private static final int NO_ARG_CONSTRUCTOR_PARAMETER_COUNT = 0;
    private static final int ID_INDEX = 1;
    private static final int REMOVE_START_BRACKET = 2;
    private static final int REMOVE_END_BRACKET = 1;
    private static final int VARIABLE_INDEX = 1;

    public void insert(String sql, Object obj) {
        List<String> parameters = extractParameters(obj, setBindVariables(sql));
        executeQuery(complySql(sql), callbackUpdate(parameters));
    }

    public void update(String sql, Object obj) {
        List<String> parameters = extractParameters(obj, setBindVariables(sql));
        executeQuery(complySql(sql), callbackUpdate(parameters));
    }

    private CallBackMethod callbackUpdate(List<String> parameters) {
        return (pstmt) -> {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setString(i + 1, parameters.get(i));
            }
            pstmt.executeUpdate();
            return null;
        };
    }

    private List<String> setBindVariables(String sql) {
        Matcher matcher = pattern.matcher(sql);
        List<String> bindVariables = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            bindVariables.add(group);
        }
        return bindVariables;
    }

    private List<String> extractParameters(Object obj, List<String> bindVariables) {
        Class<?> objClass = obj.getClass();
        Method[] declaredMethods = objClass.getDeclaredMethods();

        List<String> parameters = new ArrayList<>();
        for (String bindVariable : bindVariables) {
            addParameters(obj, declaredMethods, parameters, getGetterName(bindVariable));
        }
        return parameters;
    }

    private String getGetterName(String bindVariable) {
        String substring = bindVariable.substring(REMOVE_START_BRACKET, bindVariable.length() - REMOVE_END_BRACKET);
        return makeGetterFormat(substring.split("\\.")[VARIABLE_INDEX]);
    }

    private String makeGetterFormat(String target) {
        return "get" + target.substring(0, 1).toUpperCase(Locale.ROOT) + target.substring(1);
    }

    private void addParameters(Object obj, Method[] declaredMethods, List<String> parameters, String getterName) {
        try {
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(getterName)) {
                    parameters.add(String.valueOf(declaredMethod.invoke(obj)));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> findAll(String sql, Class<T> resultType) {
        List<T> results = new ArrayList<>();
        executeQuery(complySql(sql), callBackFindAll(resultType, results));
        return results;
    }

    private <T> CallBackMethod callBackFindAll(Class<T> resultType, List<T> results) {
        return (pstmt) -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    T result = getResult(resultType, rs);
                    if (result != null) {
                        results.add(result);
                    }
                }
            }
            return null;
        };
    }

    public <T> T findById(String sql, String id, Class<T> resultType) {
        Object executeQuery = executeQuery(complySql(sql), callbackFindById(id, resultType));
        return resultType.cast(executeQuery);
    }

    private <T> CallBackMethod callbackFindById(String id, Class<T> resultType) {
        return (pstmt) -> {
            pstmt.setString(ID_INDEX, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    T result = getResult(resultType, rs);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        };
    }

    private <T> T getResult(Class<T> resultType, ResultSet rs) throws SQLException {
        try {
            return invokeReflectionMethod(resultType, rs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T invokeReflectionMethod(Class<T> resultType, ResultSet rs)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        Constructor<?>[] constructors = resultType.getConstructors();
        Field[] declaredFields = resultType.getDeclaredFields();
        Map<String, String> fieldMap = declaredFieldMap(rs, resultType.getDeclaredFields());

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == declaredFields.length) {
                Object obj = constructor.newInstance(fieldMap.values().toArray());
                return resultType.cast(obj);
            }

            T result = invokeAsNoArgConstructor(resultType, fieldMap, constructor);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private Map<String, String> declaredFieldMap(ResultSet rs, Field[] declaredFields) throws SQLException {
        Map<String, String> fieldMap = new LinkedHashMap<>();
        for (Field declaredField : declaredFields) {
            fieldMap.put(declaredField.getName(), rs.getString(declaredField.getName()));
        }
        return fieldMap;
    }

    private <T> T invokeAsNoArgConstructor(Class<T> resultType, Map<String, String> fieldMap, Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (constructor.getParameterCount() != NO_ARG_CONSTRUCTOR_PARAMETER_COUNT) {
            return null;
        }

        Object obj = constructor.newInstance();
        Method[] declaredMethods = resultType.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            String fieldName = fieldMap.keySet().stream().filter(key -> same(declaredMethod, key)).findFirst().orElse("notExistFieldName");
            if (declaredMethod.getName().startsWith("set") && same(declaredMethod, fieldName)) {
                declaredMethod.invoke(obj, fieldMap.get(fieldName));
            }
        }
        return resultType.cast(obj);
    }

    private boolean same(Method declaredMethod, String fieldName) {
        return fieldName.toLowerCase(Locale.ROOT).equals(declaredMethod.getName().substring(3).toLowerCase());
    }

    private String complySql(String sql) {
        return sql.replaceAll(BIND_VARIABLE_REGEX, "?");
    }

    private Object executeQuery(String sql, CallBackMethod callBackMethod) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            return callBackMethod.invoke(pstmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
