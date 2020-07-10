package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class JdbcTemplate<T> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    void save(T t) {
        try (PreparedStatement pstmt = getPreparedStatement(createQuery())) {
            setValues(t, pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract PreparedStatement setValues(T t, PreparedStatement preparedStatement);

    public abstract String createQuery();
}
