package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KingCjy
 */
public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private RowMapper rowMapper;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();

        int index = 0;

        while (rs.next()) {
            result.add((T) this.rowMapper.mapRow(rs, index++));
        }

        return result;
    }
}
