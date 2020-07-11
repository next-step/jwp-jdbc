package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcTemplate<T> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    void save(QueryGenerator query, PreparedStatementSetter preparedStatementSetter) {
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            preparedStatementSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> query(QueryGenerator query, RowMapper<T> rowMapper) {
        List<T> objects = new ArrayList<>();
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                objects.add(rowMapper.mapRow(resultSet));
            }
            return objects;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T queryForObject(QueryGenerator query, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            preparedStatementSetter.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
