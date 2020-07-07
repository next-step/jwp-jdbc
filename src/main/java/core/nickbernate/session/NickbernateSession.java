package core.nickbernate.session;

import java.sql.Connection;

public class NickbernateSession implements Session {

    private Connection connection;

    public NickbernateSession(Connection connection) {
        this.connection = connection;
    }
}
