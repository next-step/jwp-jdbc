package core.nickbernate.manager;

import core.nickbernate.session.SessionFactory;

import javax.sql.DataSource;

public class DefaultEntityManagerFactory implements EntityManagerFactory {

    private DataSource dataSource;
    private SessionFactory sessionFactory = new SessionFactory();

    public DefaultEntityManagerFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public EntityManager createEntityManager() {
        return sessionFactory.openSession(dataSource);
    }

}
