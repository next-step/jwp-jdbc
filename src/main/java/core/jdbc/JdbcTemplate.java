package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum JdbcTemplate {
    INSTANCE;

    public void executeUpdate(PreparedStatementCreator preparedStatementCreator) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = preparedStatementCreator.createPreparedStatement(con))
        {
            pstmt.executeUpdate();
        }
    }

    public Object executeQueryForObject(PreparedStatementCreator psCreator, ResultSetExtractor<?> rsExtractor) throws SQLException {
        try(Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = psCreator.createPreparedStatement(con);
            ResultSet rs = pstmt.executeQuery())
        {
            return rsExtractor.extractData(rs);
        }
    }
}
