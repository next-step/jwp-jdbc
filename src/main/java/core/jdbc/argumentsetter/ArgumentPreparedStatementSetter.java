package core.jdbc.argumentsetter;

import core.jdbc.exception.SqlRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
                Object arg = this.args[index-1];
                setValue(ps, index, arg);
            });
    }

    protected void setValue(PreparedStatement ps, int paramPos, Object arg) {
        try {
            if (isStringValue(arg.getClass())) {
                ps.setString(paramPos, arg.toString());
            }
            else if (isDateValue(arg.getClass())) {
                ps.setTimestamp(paramPos, new java.sql.Timestamp(((java.util.Date) arg).getTime()));
            }
            else if (arg instanceof Calendar) {
                Calendar cal = (Calendar) arg;
                ps.setTimestamp(paramPos, new java.sql.Timestamp(cal.getTime().getTime()), cal);
            }
            else {
                ps.setObject(paramPos, arg);
            }
        }
        catch (SQLException e) {
            log.error("code: {}, message: {}", e.getErrorCode(), e.getMessage());
            throw new SqlRunTimeException(e.getMessage());
        }
    }

    private static boolean isStringValue(Class<?> type) {
        return (CharSequence.class.isAssignableFrom(type) ||
            StringWriter.class.isAssignableFrom(type));
    }

    private static boolean isDateValue(Class<?> type) {
        return (Date.class.isAssignableFrom(type) &&
            !(java.sql.Date.class.isAssignableFrom(type) ||
                Time.class.isAssignableFrom(type) ||
                Timestamp.class.isAssignableFrom(type)));
    }
}
