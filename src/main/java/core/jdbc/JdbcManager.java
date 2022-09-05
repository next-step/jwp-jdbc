package core.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcManager {
    private final Pattern pattern = Pattern.compile("[#{](.*?)[}]");

    public void insert(String sql, Object obj) {
        List<String> parameters = extractParameters(obj, setBindVariables(sql));
        executeQuery(complySql(sql), parameters);
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

    private String complySql(String sql) {
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String group = matcher.group();
            sql = sql.replace(group, "?");
        }
        return sql;
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
        String substring = bindVariable.substring(2, bindVariable.length() - 1);
        String target = substring.split("\\.")[1];
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

    private void executeQuery(String sql, List<String> parameters) {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setString(i + 1, parameters.get(i));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
