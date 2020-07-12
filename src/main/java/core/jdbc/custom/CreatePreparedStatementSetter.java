package core.jdbc.custom;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;

public class CreatePreparedStatementSetter<T> implements PreparedStatementSetter {
    T t;

    public CreatePreparedStatementSetter(T t) {
        this.t = t;
    }

    @Override
    public void setValues(final PreparedStatement preparedStatement) {

        final Field[] fields = t.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        try {
            if (preparedStatement.toString().contains("UPDATE")) {
                for (int i = 1; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    preparedStatement.setString(i, fields[i].get(t).toString());
                }
                fields[0].setAccessible(true);
                preparedStatement.setString(fields.length, fields[0].get(t).toString());
                return;
            }

            for (int i = 1; i <= fields.length; i++) {
                fields[i - 1].setAccessible(true);
                preparedStatement.setString(i, fields[i - 1].get(t).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
