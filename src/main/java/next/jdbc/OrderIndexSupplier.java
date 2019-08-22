package next.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class OrderIndexSupplier implements StatementSupplier {

    private static final int INITIAL_INDEX = 1;

    private final Queue<Object> parameters;

    public OrderIndexSupplier(final Object... parameters) {
        this.parameters = new LinkedList<>(Arrays.asList(parameters));
    }

    @Override
    public void supply(final PreparedStatement preparedStatement) throws SQLException {
        int index = INITIAL_INDEX;

        while (!parameters.isEmpty()) {
            final Object parameter = parameters.poll();

            preparedStatement.setObject(index++, parameter);
        }
    }
}
