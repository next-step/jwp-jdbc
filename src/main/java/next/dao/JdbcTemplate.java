package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract public class JdbcTemplate<T> {

    public void insertOrUpdate(PreparedStatementSetter preparedStatementSetter) throws SQLException {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
             )
        {
            preparedStatementSetter.values(pstmt);
        }
    }

    public T queryForObject(RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws SQLException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());
             )
        {
            preparedStatementSetter.values(pstmt);

            return convertToObj(rowMapper, pstmt);
        }
    }

    public T convertToObj(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        T obj = null;
        try (ResultSet rs = pstmt.executeQuery())
        {
            if (rs.next()) {
                obj = rowMapper.mapRow(rs);
            }
        }
        return obj;
    }

    public List<T> query(RowMapper<T> rowMapper) throws SQLException {

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(createQuery());)
        {
            return convertToList(rowMapper, pstmt);
        }
    }

    public List<T> convertToList(RowMapper<T> rowMapper, PreparedStatement pstmt) throws SQLException {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = pstmt.executeQuery())
        {
            while (rs.next()) {
                T findUser = rowMapper.mapRow(rs);
                list.add(findUser);
            }
        }
        return list;
    }

    abstract public String createQuery();
}
