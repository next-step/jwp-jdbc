package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.PreparedStatement;

public interface PreparedStatementSetter {
    PreparedStatement values() throws Exception;
}
