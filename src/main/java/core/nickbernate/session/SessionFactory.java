package core.nickbernate.session;

import javax.sql.DataSource;

public class SessionFactory {

    public Session openSession(DataSource dataSource) {
        return new NickbernateSession(dataSource);
    }

}
