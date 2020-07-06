package core.jdbc.entitymanager;

import core.jdbc.JdbcTemplate;
import core.jdbc.entitymanager.exception.EntityManagerExecuteException;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DefaultEntityManagerFactory implements EntityManagerFactory {

    private DataSource dataSource;

    public DefaultEntityManagerFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public EntityManager createEntityManager() {
        try {
            return new DefaultEntityManager(dataSource.getConnection(), new JdbcTemplate(dataSource));
        } catch (SQLException e) {
            throw new EntityManagerExecuteException("EntityManager Creation Failed.", e);
        }
    }
}
