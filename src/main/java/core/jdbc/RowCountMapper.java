package core.jdbc;

import java.sql.SQLException;

public class RowCountMapper implements ResultMapper<Integer, Integer> {

    @Override
    public Integer resultMapping(Integer result) throws SQLException {
        return result;
    }

}