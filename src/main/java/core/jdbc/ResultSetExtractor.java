package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<R> {

    R extractData(ResultSet rs) throws SQLException;
}
