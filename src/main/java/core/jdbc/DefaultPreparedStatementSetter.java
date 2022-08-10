package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultPreparedStatementSetter implements PreparedStatementSetter {

    private final String sql;
    private final List<Object> values = new ArrayList<>();


    public DefaultPreparedStatementSetter(String sql) {
        this(sql, Collections.emptyList());
    }

    public DefaultPreparedStatementSetter(String sql, Object... values) {
        this(sql, Arrays.stream(values).collect(toList()));
    }

    public DefaultPreparedStatementSetter(String sql, List<Object> values) {
        this.sql = sql;
        this.values.addAll(values);
    }

    @Override
    public PreparedStatement values() throws Exception {
        Connection conn = ConnectionManager.getConnection();
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            for (int i = 0; i < values.size(); i++) {
                appendStatement(i, psmt);
            }
            return psmt;
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    private void appendStatement(int idx, PreparedStatement psmt) throws SQLException {
        Object value = values.get(idx);
        Class<?> clazz = value.getClass();

        if (Integer.class == clazz || clazz.equals(Integer.TYPE)) {
            psmt.setInt(idx, (int) value);
            return;
        }

        if (clazz == Double.class || clazz.equals(Double.TYPE)) {
            psmt.setDouble(idx, (double) value);
            return;
        }

        if (clazz == Long.class || clazz.equals(Long.TYPE)) {
            psmt.setLong(idx, (long) value);
            return;
        }

        psmt.setString(idx + 1, (String) value);
    }
}
