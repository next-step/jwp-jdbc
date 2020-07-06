package core.jdbc.custom;

import java.sql.Connection;
import java.sql.PreparedStatement;

@FunctionalInterface
public interface ActionablePrepared {
    PreparedStatement getPreparedStatement(Connection connection) throws Exception;
}
