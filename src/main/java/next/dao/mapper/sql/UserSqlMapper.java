package next.dao.mapper.sql;

public class UserSqlMapper {
    public static String insert() {
        return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    }

    public static String update() {
        return "UPDATE USERS SET name=?, email=? WHERE userId=?";
    }

    public static String select() {
        return "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    }

    public static String selectAll() {
        return "SELECT userId, password, name, email FROM USERS";
    }
}
