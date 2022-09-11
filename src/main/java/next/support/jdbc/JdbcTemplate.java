package next.support.jdbc;

import core.jdbc.ConnectionManager;
import next.exception.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private static final JdbcTemplate jdbcTemplate = new JdbcTemplate();
    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("객체 생성 / 수정에 실패 하였습니다. Error Message : " + e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters);
             ResultSet rs = pstmt.executeQuery()){

            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }

            throw new DataAccessException("객체 조회에 실패 하였습니다.");

        } catch (SQLException e) {
            throw new DataAccessException("객체 조회에 실패 하였습니다. Error Message : " + e);
        }
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... queryParameters) {
        try (PreparedStatement pstmt = createPreParedStatement(sql, queryParameters);
             ResultSet rs = pstmt.executeQuery()){

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new DataAccessException("객체 조회에 실패 하였습니다. Error Message : " + e);
        }
    }

    private PreparedStatement createPreParedStatement(String sql, Object... queryParameters) throws SQLException {
        PreparedStatement pstmt = ConnectionManager.getConnection().prepareStatement(sql);
        for (int i = 0; i < queryParameters.length; i++) {
            pstmt.setObject(i + 1, queryParameters[i]);
        }
        return pstmt;
    }
}
