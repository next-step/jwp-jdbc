package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbstractJdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    protected void connectionAndModify(String query, Object... values) throws DataAccessException{
        try(
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)
        ){
            Object[] param = values;
            initValue(pstmt, param);
            pstmt.executeUpdate();
        }catch(SQLException e){
            logger.error("SQLException Error {}", e.getMessage());
            throw new DataAccessException("연결 실패!");
        }
    }

    protected void connectionAndModify(String query, PreparedStatementSetter setter) throws DataAccessException{
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

    protected List<Object> connectionAndSelect(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        try(
            Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)
        ){
            Object[] param = values;
            initValue(pstmt, param);
            return getResultItem(rowMapper, pstmt);
        }catch(SQLException e){
            logger.error("SQLException Error {}", e.getMessage());
            throw new DataAccessException("연결 실패!");
        }
    }

    protected List<Object> connectionAndSelect(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
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

    private List<Object> getResultItem(RowMapper<?> rowMapper, PreparedStatement pstmt) throws SQLException {
        ResultSet resultSet = pstmt.executeQuery();

        List<Object> itemArray = new ArrayList<>();
        while (resultSet.next()) {
            Object item = rowMapper.mapRow(resultSet);
            itemArray.add(item);
        }

        return itemArray;
    }

    private void initValue(PreparedStatement pstmt, Object[] values) throws SQLException{
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject((i+1), values[i]);
        }
    }

}
