package core.jdbc;


import next.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate implements JdbcOperations {

    @Override
    public void execute(String sql, Object... parameters) {
        ExecuteTemplate template = new ExecuteTemplate() {
            @Override
            void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                for (Object obj : parameters) {
                    ps.setString(i++, obj.toString());
                }
            }

            @Override
            <R> R getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException {
                return null;
            }
        };
        template.execute(sql);
    }

    @Override
    public <T> List<T> queryForList(String sql, ResultSetExtractor<T> resultSetExtractor) {
        ExecuteTemplate template = new ExecuteTemplate() {
            @Override
            void setValues(PreparedStatement ps) {
            }

            @Override
            List<User> getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException {
                List<User> results = new ArrayList<>();
                while (rs.next()) {
                    results.add((User) resultSetExtractor.extractData(rs));
                }
                return results;
            }
        };
        return template.execute(sql, resultSetExtractor);
    }

    @Override
    public <T> T queryForObject(String sql, ResultSetExtractor<T> resultSetExtractor, Object... parameters) {
        ExecuteTemplate template = new ExecuteTemplate() {
            @Override
            void setValues(PreparedStatement ps) throws SQLException {
                int i = 1;
                for (Object obj : parameters) {
                    ps.setString(i++, obj.toString());
                }
            }

            @Override
            User getResultMapper(ResultSetExtractor extractor, ResultSet rs) throws SQLException {
                List<User> results = new ArrayList<>();
                while (rs.next()) {
                    results.add((User) resultSetExtractor.extractData(rs));
                }
                return results.get(0);
            }
        };
        return template.execute(sql, resultSetExtractor);
    }
}