package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException, DataAccessException;
}
