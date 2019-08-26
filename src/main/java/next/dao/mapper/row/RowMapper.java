package next.dao.mapper.row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RowMapper<T> {
    public List<T> mapResult(ResultSet rs) throws SQLException;
}
