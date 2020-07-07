package core.nickbernate.manager;

import core.nickbernate.exception.NickbernateExecuteException;
import core.nickbernate.session.Session;
import core.nickbernate.session.SessionFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DefaultEntityManagerFactory implements EntityManagerFactory {

    private DataSource dataSource;
    private SessionFactory sessionFactory = new SessionFactory();

    public DefaultEntityManagerFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public EntityManager createEntityManager() {
        try {
            Session session = sessionFactory.createNickbernateSession(dataSource.getConnection());
            return new DefaultEntityManager(session);
        } catch (SQLException e) {
            throw new NickbernateExecuteException("EntityManager Creation Failed.", e);
        }
    }
}
