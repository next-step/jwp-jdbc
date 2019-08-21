package core.jdbc;

import core.jdbc.exception.JdbcSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }

    public static <T> T query(PreparedStatementCreator preparedStatementCreator, ResultMapper<T> resultMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()
        ) {
            return resultMapper.map(rs);
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }

    public static <T> T query(String sql, ResultMapper<T> resultMapper, Object... parameters) {
        PreparedStatementCreator preparedStatementCreator = PreparedStatementCreator.createByQuery(sql, parameters);

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con);
             ResultSet rs = pstmt.executeQuery()
        ) {
            return resultMapper.map(rs);
        } catch (SQLException e) {
            throw new JdbcSQLException(e);
        }
    }
}
