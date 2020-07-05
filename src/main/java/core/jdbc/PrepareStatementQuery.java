package core.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrepareStatementQuery {

    private final String query;
    private final Object[] parameters;

    public PrepareStatementQuery(String query, Object... parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    PreparedStatement create(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(this.query);

        for (int parameterIndex = 0; parameterIndex < this.parameters.length; parameterIndex++) {
            pstmt.setObject(parameterIndex + 1, this.parameters[parameterIndex]);
        }
        return pstmt;
    }
}
