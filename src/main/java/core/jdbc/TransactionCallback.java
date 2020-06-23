package core.jdbc;

/**
 * @author KingCjy
 */
public interface TransactionCallback {
    void doInTransaction();
}
