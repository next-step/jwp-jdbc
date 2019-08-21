package core.jdbc;

import core.di.factory.ClassNewInstanceUtils;
import core.di.factory.ConstructorParameters;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static core.di.factory.ParameterNameDiscoverUtils.ParameterTypeName;

public class TypeRowMapper<T> implements RowMapper<T> {

    private Class<T> parameterizedType;

    public TypeRowMapper(Class<T> parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    public T resultMapping(ResultSet rs) throws SQLException {
        String[] columnNames = getColumnNames(rs);
        ConstructorParameters constructorParameters = ClassNewInstanceUtils.getContructorParameters(this.parameterizedType, columnNames);
        Constructor constructor = constructorParameters.getConstructor();
        Object[] args = getArgumentValues(rs, constructorParameters.getParameterTypeNames());

        try {
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getArgumentValues(ResultSet rs, List<ParameterTypeName> parameterTypeNames) throws SQLException {
        JdbcTypeHandlers jdbcTypeHandlers = JdbcTypeHandlers.getInstance();
        Object[] args = new Object[parameterTypeNames.size()];
        for(int i = 0 ; i < parameterTypeNames.size(); i ++) {
            ParameterTypeName typeName = parameterTypeNames.get(i);
            args[i] = jdbcTypeHandlers.getTypeHandler(typeName.getType()).getParameter(rs, typeName.getName());
        }
        return args;
    }

    private String[] getColumnNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        String[] columnNames = new String[metaData.getColumnCount()];
        for(int i = 0; i < columnNames.length; i ++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
        }
        return columnNames;
    }



}