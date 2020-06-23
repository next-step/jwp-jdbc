package core.jdbc;

import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author KingCjy
 */
public class TransactionTemplate {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTemplate.class);

    private DataSource dataSource;

    public TransactionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(TransactionCallback callback) {
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            logger.info("Transaction Start");
            connection.setAutoCommit(false);
            callback.doInTransaction();

            connection.commit();
            logger.info("Transactin Commit");
        } catch (SQLException | DataAccessException e) {
            try {
                connection.rollback();
                logger.info("Rollback Called");
            } catch (SQLException ex) {
                logger.error("SQLException", ex);
            }
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
            DataSourceUtils.releaseConnection(connection);
            logger.info("Release Connection");
        }
    }
}
