package study.jdbc;

import core.jdbc.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@DisplayName("CRUD R를 제외하고는 execute로 통일이 가능하다.")
public class crud {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    @DisplayName("create 는 execute 로 실행가능")
    void create() throws SQLException { //insert update or execute
        PreparedStatement preparedStatement = prepareStatement("INSERT INTO USERS VALUES('123', '123', '123', '123')");
        boolean result = preparedStatement.execute();

        System.out.println(result);
    }

    @Test
    @DisplayName("read 는 executeQuery 로 result set 을 받아올 수 있다")
    void read() throws SQLException {
        PreparedStatement preparedStatement = prepareStatement("SELECT * FROM USERS");
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println(resultSet);
        while (resultSet.next()) {
            System.out.println(resultSet);
        }
    }

    @Test
    @DisplayName("update 로 execute 로 실행가능")
    void update() throws SQLException { // INSERT INTO USERS VALUES('admin', 'password', '자바지기', 'admin@slipp.net');
        PreparedStatement preparedStatement = prepareStatement("UPDATE USERS SET name = ? where userId = ?");
        preparedStatement.setObject(1, "hi");
        preparedStatement.setObject(2, "admin");

        boolean execute = preparedStatement.execute();
        System.out.println(execute);
    }

    @Test
    @DisplayName("delete 는 execute로 실행가능")
    void delete() throws SQLException {
        PreparedStatement preparedStatement = prepareStatement("DELETE FROM USERS WHERE userId = ?");
        preparedStatement.setObject(1, "admin");

        boolean execute = preparedStatement.execute();
        System.out.println(execute);
    }

    private PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = ConnectionManager.getConnection();

        return connection.prepareStatement(sql);
    }
}
