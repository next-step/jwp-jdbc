package core.jdbc.custom;

import core.jdbc.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractRepository implements Repository<Object, String> {

    private Connection getConnection() {
        return ConnectionManager.getConnection();
    }

    private PreparedStatement getPreparedStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(final Object object) {
        final Field[] fields = object.getClass()
                .getDeclaredFields();

        String marks = getQuestionMark(fields.length);
        String sql = String.format("INSERT INTO USERS VALUES (%s)", marks);

        try (PreparedStatement pstmt = getPreparedStatement(sql)) {
            for (int i = 1; i <= fields.length; i++) {
                fields[i - 1].setAccessible(true);
                pstmt.setString(i, fields[i - 1].get(object).toString());
            }
            final int i = pstmt.executeUpdate();
//            ResultSet resultSet = pstmt.executeQuery();
//            if (resultSet.next()) {
//                Object[] objects = new Object[fields.length];
//                for (int i = 1; i <= fields.length; i++) {
//                    objects[i-1] = resultSet.getString(i);
//                }
//
//                Constructor<?> declaredConstructor = Arrays.stream(user.getClass().getConstructors())
//                        .max((o1, o2) -> Math.max(o1.getParameterCount(), o2.getParameterCount()))
//                        .get();

//            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getQuestionMark(int size) {
        StringBuilder questionMarks = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            questionMarks.append("?, ");
        }
        questionMarks.append("?");
        return questionMarks.toString();
    }
}
