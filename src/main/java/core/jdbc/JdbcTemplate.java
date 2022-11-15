package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract public class JdbcTemplate<T> {

    public void update(PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
        ) {
            preparedStatementSetter.values(pstmt);

            pstmt.executeUpdate();
        }
    }

    public void update(Object... values) throws SQLException {
        update(createPreparedStatementSetter(values));
    }

    public T queryForObject(RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        return getObject(query(rowMapper, preparedStatementSetter));
    }

    public T queryForObject(RowMapper<T> rowMapper, Object... values) throws SQLException {
        return getObject( query(rowMapper, createPreparedStatementSetter(values)));
    }

    private T getObject(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    public List<T> query(RowMapper<T> rowMapper) throws SQLException {
        return query(rowMapper, null);
    }

    public List<T> query(RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery())) {

            if(preparedStatementSetter != null) {
                preparedStatementSetter.values(pstmt);
            }

            return convertToList(rowMapper, pstmt);
        }
    }

    private List<T> convertToList(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                T findUser = rowMapper.mapRow(rs);
                list.add(findUser);
            }
        }
        return list;
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object[] values) {
        return ps -> {
            for (int i = 0; i < values.length; ++i) {
                ps.setObject(i + 1, values[i]);
            }
        };
    }

    abstract public String createQuery();
}
