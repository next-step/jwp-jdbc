package core.jdbc;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.util.List;

public class JdbcTemplate {

    public void insert(String query, Object... values) throws DataAccessException {
        JdbcConnection.connectionAndModify(query, values);
    }

    public void insert(String query, PreparedStatementSetter setter) throws DataAccessException {
        JdbcConnection.connectionAndModify(query, setter);
    }

    public void update(String query, Object... values) throws DataAccessException {
        JdbcConnection.connectionAndModify(query, values);
    }

    public void update(String query, PreparedStatementSetter setter) throws DataAccessException {
        JdbcConnection.connectionAndModify(query, setter);
    }

    public <T> List<T> queryForList(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        return JdbcConnection.connectionAndSelect(query, rowMapper, values);
    }

    public <T> List<T> queryForList(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
        return JdbcConnection.connectionAndSelect(query, rowMapper, setter);
    }

    public <T> T queryForObject(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        return getReturnObject(JdbcConnection.connectionAndSelect(query, rowMapper, values));
    }

    public <T> T queryForObject(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
        return getReturnObject(JdbcConnection.connectionAndSelect(query, rowMapper, setter));
    }

    private <T> T getReturnObject(List<T> items){
        if(items.size() > 1){
            throw new IncorrectResultSizeDataAccessException(1);
        }

        return items.get(0);
    }

}
