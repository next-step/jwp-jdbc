package core.jdbc;

import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;
    private final int expectedRows;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int expectedRows) {
        this.rowMapper = rowMapper;
        this.expectedRows = expectedRows;
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<T> results = createEmptyResults();
        while (rs.next()) {
            results.add(rowMapper.mapRow(rs));
        }
        return results;
    }

    private List<T> createEmptyResults() {
        if (expectedRows > 0) {
            return new ArrayList<>(expectedRows);
        }
        return new ArrayList<>();
    }
}
