package core.nickbernate.session;

public interface Session {

    <T> void persist(Object id, T entity);

}
