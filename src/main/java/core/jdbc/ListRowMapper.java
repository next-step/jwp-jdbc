package core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListRowMapper<T> implements ResultMapper<ResultSet, List<T>> {

    private final RowMapper<T> rowMapper;

    public ListRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<T> resultMapping(ResultSet rs) throws SQLException {

        List<T> tempList = new ArrayList<>();

        while(rs.next()) {
            tempList.add(rowMapper.resultMapping(rs));
        }

        return tempList;
    }
}