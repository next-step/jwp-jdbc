package core.jdbc;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by iltaek on 2020/07/06 Blog : http://blog.iltaek.me Github : http://github.com/iltaek
 */
public class JdbcTemplate {

    private static final String ERROR_ON_UPDATE = "update 과정 중에 문제가 있습니다.";
    private static final String ERROR_ON_QUERY_FOR_LIST = "queryForList 과정 중에 문제가 있습니다.";

    private JdbcTemplate() {
    }

    public static void update(String sql, Object... params) {
        update(sql, createPreparedStatementSetter(params));
    }

    public static void update(String sql, PreparedStatementSetter pss) {
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setValue(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(ERROR_ON_UPDATE, e);
        }
    }

    private static PreparedStatementSetter createPreparedStatementSetter(Object[] params) {
        return pstmt -> {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        };
    }

    public static <T> List<T> queryForList(String sql, RowMapper<T> rm, Object... params) {
        return queryForList(sql, rm, createPreparedStatementSetter(params));
    }

    public static <T> List<T> queryForList(String sql, RowMapper<T> rm, PreparedStatementSetter pss) {
        try (
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)
        ) {
            pss.setValue(pstmt);
            return mapList(pstmt, rm);
        } catch (SQLException e) {
            throw new DataAccessException(ERROR_ON_QUERY_FOR_LIST, e);
        }
    }

    private static <T> List<T> mapList(PreparedStatement pstmt, RowMapper<T> rm) throws SQLException {
        List<T> results = Lists.newArrayList();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                results.add(rm.mapRow(rs));
            }
        }
        return results;
    }

    public static <T> T queryForObject(String sql, RowMapper<T> rm, Object... params) {
        return queryForList(sql, rm, createPreparedStatementSetter(params)).get(0);
    }
}
