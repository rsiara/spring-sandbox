import configuration.RootConfig;
import model.MessageLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/*


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryAdvancedSqlInsertDeleteTest {

    private final String INSERT_SQL = "INSERT INTO message_log (message, log_dttm) VALUES(?, CURRENT TIMESTAMP)";
    private final String DELETE_SQL = "DELETE FROM message_log";
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void sql_insert_delete_test() {
        System.out.println(" *** Advanced queries - sql insert and delete ***");

        logMessage("Dump message");

        System.out.println("After log message: ");
        for (Object o : findAllMessages()) {
            MessageLog messageLog = (MessageLog) o;
            System.out.println(messageLog);
        }

        System.out.println("After clear message: ");
        clearMessageLog();

        for (Object o : findAllMessages()) {
            MessageLog messageLog = (MessageLog) o;
            System.out.println(messageLog);
        }
    }

    public void logMessage(String message) {
        entityManager.createNativeQuery(INSERT_SQL)
                .setParameter(1, message)
                .executeUpdate();
    }

    public void clearMessageLog() {
        entityManager.createNativeQuery(DELETE_SQL)
                .executeUpdate();
    }

    public List findAllMessages() {
        Query query = entityManager.createQuery("SELECT m FROM MessageLog m");
        return query.getResultList();
    }
}