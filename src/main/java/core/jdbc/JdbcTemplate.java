package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    public int execute(PrepareStatementQuery query) {
        return prepareQuery(query, pstmt -> pstmt.executeUpdate());
    }

    public <T> T queryOne(PrepareStatementQuery query, RowMapper<T> rowMapper) {
        return prepareQuery(query, pstmt -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.getFetchSize() > 1) {
                    throw new DataAccessException("not one");
                }

                while (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
                return null;
            }
        });
    }

    public <T> List<T> queryList(PrepareStatementQuery query, RowMapper<T> rowMapper) {
        return prepareQuery(query, pstmt -> {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.getFetchSize() > 1) {
                    throw new DataAccessException("not one");
                }
                if (rs.getFetchSize() > 1) {
                    throw new DataAccessException("not one");
                }

                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rowMapper.mapRow(rs));
                }
                return list;
            }
        });
    }

    private <T> T prepareQuery(PrepareStatementQuery query, PrepareStatementCallback<T> callback) {
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = query.create(con);
        ) {
            return callback.doInPrepareStatement(pstmt);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
