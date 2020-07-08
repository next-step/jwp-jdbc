package core.nickbernate.session;

import java.sql.Connection;

public class SessionFactory {

    public Session createNickbernateSession(Connection connection) {
        return new NickbernateSession(connection);
    }

}
