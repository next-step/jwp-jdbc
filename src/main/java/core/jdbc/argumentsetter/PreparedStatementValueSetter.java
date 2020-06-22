package core.jdbc.argumentsetter;

import java.sql.SQLException;

@FunctionalInterface
interface PreparedStatementValueSetter<A, B, C> {
    void accept(A a, B b, C c) throws SQLException;
}