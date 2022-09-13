package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPreparedStatementSetter<E> implements PreparedStatementSetter<PreparedStatement> {

    private final List<E> parameters;

    public DefaultPreparedStatementSetter(E... parameters) {
        this.parameters = Arrays.stream(parameters)
                .collect(Collectors.toList());
    }

    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        int index = 0;
        for (E parameter : parameters) {
            preparedStatement.setObject(index + 1, parameter);
            index++;
        }
    }
}
