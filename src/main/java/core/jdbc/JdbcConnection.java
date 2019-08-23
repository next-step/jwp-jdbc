package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcConnection {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public static void connectionAndModify(String query, Object... values) throws DataAccessException{
        connectionAndModify(query, ps -> {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i+1, values[i]);
            }
        });
    }

    public static void connectionAndModify(String query, PreparedStatementSetter setter) throws DataAccessException{
        try(
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)
        ){
            setter.setValues(pstmt);
            pstmt.executeUpdate();
        }catch(SQLException e){
            logger.error("SQLException Error {}", e.getMessage());
            throw new DataAccessException("연결 실패!");
        }
    }

    public static <T> List<T> connectionAndSelect(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        return connectionAndSelect(query, rowMapper, ps -> {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i+1, values[i]);
            }
        });
    }

    public static <T> List<T> connectionAndSelect(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
        try(
                Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)
        ){
            setter.setValues(pstmt);
            return getResultItem(rowMapper, pstmt);
        }catch(SQLException e){
            logger.error("SQLException Error {}", e.getMessage());
            throw new DataAccessException("연결 실패!");
        }
    }

    private static <T> List<T> getResultItem(RowMapper<?> rowMapper, PreparedStatement pstmt) throws SQLException {
        ResultSet resultSet = pstmt.executeQuery();

        List<T> itemArray = new ArrayList<>();
        while (resultSet.next()) {
            itemArray.add((T) rowMapper.mapRow(resultSet));
        }

        return itemArray;
    }
    
}
