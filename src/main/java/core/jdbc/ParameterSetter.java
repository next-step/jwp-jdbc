package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterSetter {

    private final Object[] args;
    private final  int[] argTypes;

    public ParameterSetter(Object[] args, int[] argTypes) {
        this.args = args;
        this.argTypes = argTypes;
    }

    public void setArgs(PreparedStatement ps) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            ps.setObject(i+1, arg, argTypes[i]);
        }
    }
}