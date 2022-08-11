package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.sql.Statement;

@FunctionalInterface
public interface StatementCallback<T> {

    T doInStatement(Statement stmt) throws SQLException, DataAccessException;
}
