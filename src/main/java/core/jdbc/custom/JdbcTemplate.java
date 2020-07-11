package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class JdbcTemplate {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) throws Exception {
        ActionablePrepared actionablePrepared = (connection) -> connection.prepareStatement(sql);
        return actionablePrepared.getPreparedStatement(getConnection());
    }

    void save(String query) {
        try (PreparedStatement pstmt = getPreparedStatement(query)) {
            setValues(pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object> query(String query) {
        List<Object> objects = new ArrayList<>();
        try (PreparedStatement pstmt = getPreparedStatement(query)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                objects.add(mapRow(resultSet));
            }
            return objects;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object queryForObject(String query) {
        try (PreparedStatement pstmt = getPreparedStatement(query)) {
            setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public abstract void setValues(PreparedStatement preparedStatement);

    public abstract Object mapRow(ResultSet resultSet);
}
