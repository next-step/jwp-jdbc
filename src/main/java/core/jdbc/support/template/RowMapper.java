package core.jdbc.support.template;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper {
	Object mapRow(ResultSet rs) throws SQLException;
}
