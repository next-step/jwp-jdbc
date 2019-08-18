package core.jdbc;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {

    public static void insert(String query, Object... values) throws SQLException {
        modify(query, values);
    }

    public static void update(String query, Object... values) throws SQLException {
        modify(query, values);
    }

    private static void modify(String query, Object... values) throws SQLException{
        init(query, values).executeUpdate();
    }

    public static ResultSet select(String query, Object... values) throws SQLException{
        return init(query, values).executeQuery();
    }

    private static PreparedStatement init(String query, Object... values) throws SQLException{
        Connection con = null;
        PreparedStatement pstmt = null;
        con = ConnectionManager.getConnection();
        pstmt = con.prepareStatement(query);
        initValue(pstmt, values);
        return pstmt;
    }

    private static void initValue(PreparedStatement pstmt, Object... values) throws SQLException{
        if(values == null){
            return;
        }

        for (int i = 0; i < values.length; i++) {
            initVariableValue(pstmt, values[i], (i+1));
        }
    }

    private static void initVariableValue(PreparedStatement pstmt, Object value, int index) throws SQLException{
        if(value instanceof Integer){
            pstmt.setInt(index, Integer.parseInt(value.toString()));
        }else{
            pstmt.setString(index, String.valueOf(value));
        }
    }

    /*

    private static void initClassValue(PreparedStatement pstmt, Object value, int index) throws SQLException{

    }

    private static boolean isClass(Object object){
        if(object instanceof Integer){
            return false;
        }

        if(object instanceof String){
            return false;
        }

        if(object instanceof Long){
            return false;
        }

        if (object instanceof Double){
            return false;
        }

        if (object instanceof Float){
            return false;
        }

        if (object instanceof Boolean){
            return false;
        }

        if (object instanceof Byte){
            return false;
        }

        if (object instanceof Short){
            return false;
        }

        return true;
    }*/

}
