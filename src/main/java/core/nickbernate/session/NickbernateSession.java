package core.nickbernate.session;

import java.sql.Connection;

public class NickbernateSession implements Session {

    private Connection connection;

    public NickbernateSession(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> void persist(Object id, T entity) {
        // TODO: 2020/07/08 cache와 snapshot 을 묶어서 저장하면 훨씬 절약
    }

}
