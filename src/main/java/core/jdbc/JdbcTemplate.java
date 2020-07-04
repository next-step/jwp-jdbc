package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public int execute(String sql, Object... parameters) throws SQLException {
        return prepareQuery(pstmt -> pstmt.executeUpdate(), sql, parameters);
    }

    public <T> T queryOne(RowMapper<T> rowMapper, String sql, Object... parameters)
        throws SQLException {
        return prepareQuery(pstmt -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.getFetchSize() > 1) {
                    throw new JdbcTemplateException("not one");
                }

                while (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
                return null;
            }
        }, sql, parameters);
    }

    public <T> List<T> queryList(RowMapper<T> rowMapper, String sql, Object... parameters)
        throws SQLException {

        return prepareQuery(pstmt -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.getFetchSize() > 1) {
                    throw new JdbcTemplateException("not one");
                }
                if (rs.getFetchSize() > 1) {
                    throw new JdbcTemplateException("not one");
                }

                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rowMapper.mapRow(rs));
                }

                return list;
            }
        }, sql, parameters);
    }

    private <T> T prepareQuery(PrepareStatementCallback<T> callback, String sql,
        Object... parameters) throws SQLException {
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
                pstmt.setObject(parameterIndex + 1, parameters[parameterIndex]);
            }

            return callback.doInPrepareStatement(pstmt);
        }
    }
}
