package core.jdbc.argumentsetter;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                setValue(ps, i + 1, arg);
            }
        }
    }

    protected void setValue(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
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
