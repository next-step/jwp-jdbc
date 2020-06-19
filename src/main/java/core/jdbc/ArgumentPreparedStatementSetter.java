package core.jdbc;

import org.springframework.jdbc.core.StatementCreatorUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] args;

    public ArgumentPreparedStatementSetter(final Object[] args) {
        this.args = args;
    }

    protected void doSetValue(PreparedStatement ps, int parameterPosition, int argType, Object argValue) throws SQLException {
        StatementCreatorUtils.setParameterValue(ps, parameterPosition, argType, argValue);
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        int parameterPosition = 1;
        if (this.args != null && this.argTypes != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                if (arg instanceof Collection && this.argTypes[i] != Types.ARRAY) {
                    Collection<?> entries = (Collection<?>) arg;
                    for (Object entry : entries) {
                        if (entry instanceof Object[]) {
                            Object[] valueArray = ((Object[]) entry);
                            for (Object argValue : valueArray) {
                                doSetValue(ps, parameterPosition, this.argTypes[i], argValue);
                                parameterPosition++;
                            }
                        }
                        else {
                            doSetValue(ps, parameterPosition, this.argTypes[i], entry);
                            parameterPosition++;
                        }
                    }
                }
                else {
                    doSetValue(ps, parameterPosition, this.argTypes[i], arg);
                    parameterPosition++;
                }
            }
        }
    }
}
