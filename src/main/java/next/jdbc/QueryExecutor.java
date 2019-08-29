package next.jdbc;

import core.jdbc.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {
    private static final Logger logger = LoggerFactory.getLogger(QueryExecutor.class);

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

    public <T> T executeScalar(final String sql, final ThrowableRowMapper<T> rowMapper, final Object... queryParams) {
        return this.executeScalar(
                sql,
                rowMapper,
                PreparedStatementParameterSetterCreator.create(queryParams));
    }

    public int executeUpdate(final String sql, final PreparedStatementParameterSetter parameterSetter) {
        try (
                final Connection con = ConnectionManager.getConnection();
                final PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            parameterSetter.setParameters(pstmt);
            return pstmt.executeUpdate();
        } catch (final SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int executeUpdate(final String sql, final Object... queryParams) {
        return this.executeUpdate(sql, PreparedStatementParameterSetterCreator.create(queryParams));
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

    public <T> List<T> executeQuery(final String sql, final RowMapper<T> rowMapper, final Object... queryParams) {
        return this.executeQuery(
                sql,
                rowMapper,
                PreparedStatementParameterSetterCreator.create(queryParams));
    }
}
