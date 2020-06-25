package core.jdbc.argumentsetter;

import core.jdbc.exception.SqlRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.IntStream;

@Slf4j
public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SqlRunTimeException {
        if (ArrayUtils.isEmpty(args)) {
            return;
        }

        IntStream.rangeClosed(1, args.length)
            .forEach(index -> {
                Object arg = this.args[index - 1];
                setValue(ps, index, arg);
            });
    }

    protected void setValue(PreparedStatement ps, int paramPos, Object arg) {
        PreparedStatementType foundType = Arrays.stream(PreparedStatementType.values())
            .filter(type -> type.getChecker().apply(arg))
            .findFirst()
            .get();

        invokeSetter(foundType, ps, paramPos, arg);
    }

    private void invokeSetter(
        PreparedStatementType type,
        PreparedStatement ps,
        int paramPos,
        Object arg
    ) {
        try {
            type.accept(ps, paramPos, arg);
        }
        catch (SQLException e) {
            log.error("code: {}, message: {}", e.getErrorCode(), e.getMessage());
            throw new SqlRunTimeException(e.getMessage());
        }
    }
}
