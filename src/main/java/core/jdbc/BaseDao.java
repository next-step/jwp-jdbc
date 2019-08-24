package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
    protected void save(String sql, Object... parameters) {
        execute(sql, (PreparedStatement pstmt) -> {
            try {
                return pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataSQLException(e);
            }
        }, parameters);
    }

    protected Object select(String sql, Mapper mapper, Object... parameters) {
        return execute(sql, (PreparedStatement pstmt) -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                return mapper.map(rs);
            } catch (SQLException e) {
                throw new DataSQLException(e);
            }
        }, parameters);
    }

    protected Object selectByObject(String sql, Mapper mapper, Object... parameters) {
        return selectByList(sql, mapper, parameters).stream()
                .findFirst()
                .orElse(null);
    }

    protected List selectByList(String sql, Mapper mapper, Object... parameters) {
        return (List) execute(sql, (PreparedStatement pstmt) -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                List results = new ArrayList();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            } catch (SQLException e) {
                throw new DataSQLException(e);
            }
        }, parameters);
    }

    private Object execute(String sql, QueryExecutor executor, Object... parameters) {
        try (
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            setValues(pstmt, parameters);
            return executor.executor(pstmt);
        } catch (SQLException e) {
            throw new DataSQLException(e);
        }
    }

    private void setValues(PreparedStatement pstmt, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            pstmt.setString(i + 1, (String) parameters[i]);
        }
    }
}
