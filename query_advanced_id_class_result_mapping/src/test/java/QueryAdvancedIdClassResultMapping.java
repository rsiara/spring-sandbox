import configuration.RootConfig;
import model.Employee;
import org.junit.Before;
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
public class QueryAdvancedIdClassResultMapping {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        Employee mark = new Employee();
        mark.setId(11);
        mark.setName("Mark");

        mark.setCountry("Poland");

        Employee john = new Employee();
        john.setId(12);
        john.setName("John");
        john.setCountry("Germany");

        mark.getDirects().add(john);

        john.setManager(mark);

        entityManager.persist(mark);
        entityManager.persist(john);
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void sql_result_set_mapping_test() {
        System.out.println(" *** Advanced queries - id class result mapping***");

     /*   System.out.println(" - Find employees with manager: ");
        for (Object result : findEmployeeWithManager()) {
            System.out.println(result);
        }*/
    }

    public List findEmployeeWithManager() {
        Query query = entityManager.createNativeQuery(
                "SELECT e.country, e.id, e.name, " +
                        "e.manager_country, e.manager_id, m.country AS mgr_country, " +
                        "m.id AS mgr_id, m.name AS mgr_name," +
                        "m.manager_country AS mgr_mgr_country, m.manager_id AS mgr_mgr_id " +
                        "FROM   emp e, emp m ",
                "EmployeeAndManager");
        return query.getResultList();
    }


}