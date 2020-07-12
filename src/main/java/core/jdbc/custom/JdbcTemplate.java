package core.jdbc.custom;

import core.jdbc.ConnectionManager;
import core.jdbc.custom.query.QueryGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate<T> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    public void save(QueryGenerator query, PreparedStatementSetter preparedStatementSetter) {
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            preparedStatementSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(QueryGenerator query, T... t) {
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            for (int i = 1; i <= t.length; i++) {
                pstmt.setObject(i, t[i - 1]);
            }

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
                objects.add(rowMapper.mapRowForObject(resultSet));
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
                return rowMapper.mapRowForObject(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T queryForObject(QueryGenerator query, RowMapper<T> rowMapper, T... t) {
        try (PreparedStatement pstmt = getPreparedStatement(query.make())) {
            for (int i = 1; i <= t.length; i++) {
                pstmt.setObject(i, t[i - 1]);
            }
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return rowMapper.mapRowForObject(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
