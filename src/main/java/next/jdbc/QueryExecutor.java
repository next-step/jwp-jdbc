package next.jdbc;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {

    public <T> T executeScalar(
            final String sql,
            final ThrowableRowMapper<T> rowMapper,
            final PreparedStatementParameterSetter parameterSetter) {
        try (
                final Connection con = ConnectionManager.getConnection();
                final PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            parameterSetter.setParameters(pstmt);
            final ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            return null;
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> List<T> executeQuery(
            final String sql,
            final RowMapper<T> rowMapper,
            final PreparedStatementParameterSetter parameterSetter) {
        try (
                final Connection con = ConnectionManager.getConnection();
                final PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            parameterSetter.setParameters(pstmt);
            final ResultSet rs = pstmt.executeQuery();
            final List<T> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(rowMapper.mapRow(rs));
            }
            return rows;
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
