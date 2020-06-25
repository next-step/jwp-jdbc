package core.jdbc.argumentsetter;

import lombok.Getter;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

public enum PreparedStatementType {
    STRING(PreparedStatementType::isStringType, PreparedStatementType::setString),
    DATE(PreparedStatementType::isDateValue, PreparedStatementType::setTimestamp),
    CALENDAR(PreparedStatementType::isCalendar, PreparedStatementType::setCalendar),
    OBJECT((arg) -> true, PreparedStatementType::setObject),
    ;

    @Getter
    private final Function<Object, Boolean> checker;

    @Getter
    private final PreparedStatementValueSetter<PreparedStatement, Integer, Object> setter;

    PreparedStatementType(Function checker, PreparedStatementValueSetter<PreparedStatement, Integer, Object> setter) {
        this.checker = checker;
        this.setter = setter;
    }

    private static boolean isStringType(Object arg) {
        Class<?> type = arg.getClass();

        return CharSequence.class.isAssignableFrom(type) ||
            StringWriter.class.isAssignableFrom(type);
    }

    private static boolean isDateValue(Object arg) {
        Class<?> type = arg.getClass();

        return (Date.class.isAssignableFrom(type) &&
            !(java.sql.Date.class.isAssignableFrom(type) ||
                Time.class.isAssignableFrom(type) ||
                Timestamp.class.isAssignableFrom(type)));
    }

    private static boolean isCalendar(Object arg) {
        return arg instanceof Calendar;
    }

    private static void setString(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
        ps.setString(paramPos, arg.toString());
    }

    private static void setTimestamp(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
        ps.setTimestamp(paramPos, new Timestamp(((Date)arg).getTime()));
    }

    private static void setCalendar(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
        Calendar cal = (Calendar) arg;
        ps.setTimestamp(paramPos, new java.sql.Timestamp(cal.getTime().getTime()), cal);
    }

    private static void setObject(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
        ps.setObject(paramPos, arg);
    }

    public void accept(PreparedStatement ps, int paramPos, Object arg) throws SQLException {
        this.setter.accept(ps, paramPos, arg);
    }
}
