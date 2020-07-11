package core.jdbc;

import core.jdbc.exception.JdbcRuntimeException;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    public void execute(String sql, Object... args) throws JdbcRuntimeException {
        execute(sql, createPreparedStatementSetter(args));
    }

    public void execute(String sql, PreparedStatementSetter pstmtSetter) throws JdbcRuntimeException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
            pstmtSetter.values(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws JdbcRuntimeException {
        return queryForObject(sql, rowMapper, createPreparedStatementSetter(args));
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) throws JdbcRuntimeException {
        List<T> result = queryForList(sql, rowMapper, pstmtSetter);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        if (result.size() > 1) {
            throw new IllegalArgumentException("데이터가 2건 이상 조회될 수 없습니다.");
        }
        return Optional.of(result.get(0));
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) throws JdbcRuntimeException {
        List<T> rows = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);) {
            pstmtSetter.values(pstmt);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rows.add(rowMapper.mapRow(rs));
            }
            return rows;
        } catch (SQLException e) {
            throw new JdbcRuntimeException(e);
        }
    }
    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... args) throws JdbcRuntimeException {
        return queryForList(sql, rowMapper, createPreparedStatementSetter(args));
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object[] args) {
        return pstmt -> {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
        };
    }

}
