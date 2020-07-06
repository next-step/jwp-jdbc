package core.jdbc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionManager {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem://localhost/~/jwp-jdbc;DB_CLOSE_DELAY=-1";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);
        return ds;
    }

}
