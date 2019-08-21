package core.jdbc;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.util.List;

public class JdbcTemplate extends AbstractJdbcTemplate{

    public void insert(String query, Object... values) throws DataAccessException {
        modify(query, values);
    }

    public void insert(String query, PreparedStatementSetter setter) throws DataAccessException {
        modify(query, setter);
    }

    public void update(String query, Object... values) throws DataAccessException {
        modify(query, values);
    }

    public void update(String query, PreparedStatementSetter setter) throws DataAccessException {
        modify(query, setter);
    }

    public List<?> queryForList(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        return connectionAndSelect(query, rowMapper, values);
    }

    public List<?> queryForList(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
        return connectionAndSelect(query, rowMapper, setter);
    }

    public Object queryForObject(String query, RowMapper<?> rowMapper, Object... values) throws DataAccessException{
        return getReturnObject(connectionAndSelect(query, rowMapper, values));
    }

    public Object queryForObject(String query, RowMapper<?> rowMapper, PreparedStatementSetter setter) throws DataAccessException{
        return getReturnObject(connectionAndSelect(query, rowMapper, setter));
    }

    private void modify(String query, Object... values) throws DataAccessException{
        connectionAndModify(query, values);
    }

    private void modify(String query, PreparedStatementSetter setter) throws DataAccessException{
        connectionAndModify(query, setter);
    }

    private Object getReturnObject(List<?> items){
        if(items.size() > 1){
            throw new IncorrectResultSizeDataAccessException(1);
        }

        return items.get(0);
    }

}
