package core.jdbc.custom;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class InsertJdbcTemplate<T> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    public void insert(T t) {
        try (PreparedStatement pstmt = getPreparedStatement(createQueryForInsert())) {
            setValuesForInsert(pstmt, t);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public abstract String createQueryForInsert();

    public abstract PreparedStatement setValuesForInsert(PreparedStatement preparedStatement, Object object) throws IllegalAccessException, SQLException;
}
