package core.jdbc;

import core.jdbc.exception.JdbcSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hspark on 2019-08-22.
 */
public class JdbcTemplate {
    public static void command(PreparedStatementCreator preparedStatementCreator) {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con)
        ) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }

    public static void command(String sql, Object... parameters) {
        PreparedStatementCreator preparedStatementCreator = PreparedStatementCreator.createByQuery(sql, parameters);

        command(preparedStatementCreator);
    }

    public static <T> T queryForObject(PreparedStatementCreator preparedStatementCreator, ResultMapper<T> resultMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()
        ) {
            return resultMapper.map(rs);
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }

    public static <T> T queryForObject(String sql, ResultMapper<T> resultMapper, Object... parameters) {
        PreparedStatementCreator preparedStatementCreator = PreparedStatementCreator.createByQuery(sql, parameters);

        return queryForObject(preparedStatementCreator, resultMapper);
    }

    public static <T> List<T> queryForList(PreparedStatementCreator preparedStatementCreator, ResultMapper<T> resultMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()
        ) {
            List<T> results = new ArrayList<>();
            while (rs.next()) {
                results.add(resultMapper.map(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }

    public static <T> List<T> queryForList(String sql, ResultMapper<T> resultMapper, Object... parameters) {
        PreparedStatementCreator preparedStatementCreator = PreparedStatementCreator.createByQuery(sql, parameters);

        return queryForList(preparedStatementCreator, resultMapper);
    }
}
