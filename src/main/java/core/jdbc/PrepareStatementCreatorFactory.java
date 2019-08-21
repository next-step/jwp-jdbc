package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrepareStatementCreatorFactory implements PreparedStatementCreator {

    private static final Logger logger = LoggerFactory.getLogger(PreparedStatementCreator.class);

    private String sql;
    private SqlParameters parameters;

    PrepareStatementCreatorFactory(String sql, SqlParameters parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            setValues(ps);
        } catch (SQLException e) {
            throw new BadSqlGrammarException("Create prepared statement", sql, e);
        }
        return ps;
    }

    private void setValues(PreparedStatement ps) throws SQLException {
        for (SqlParameters.SqlParameter parameter : parameters.toList()) {
            ps.setString(parameter.getIndex(), parameter.getValue());
        }
        logger.debug("Sql : {}, parameters : {}", sql, parameters);
    }
}
