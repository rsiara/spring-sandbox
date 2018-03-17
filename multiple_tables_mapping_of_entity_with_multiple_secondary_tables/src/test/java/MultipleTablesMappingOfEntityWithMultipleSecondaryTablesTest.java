import configuration.RootConfig;
import model.Employee;
import org.apache.commons.lang3.RandomUtils;
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

/*
In addition to the EMPLOYEE table, there are two secondary tables, ORG_STRUCTURE
and EMP_LOB. The ORG_STRUCTURE table stores employee and manager reporting information. The EMP_LOB table stores
large objects that are infrequently fetched during normal query options. Moving large objects to a secondary table is a
common design technique in many database schemas.
*/

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MultipleTablesMappingOfEntityWithMultipleSecondaryTablesTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void multiple_tables_mapping_of_entity_with_multiple_secondary_tables() {
        System.out.println(" *** Mapping entity across multiple secondary tables ***");

        Employee mark = new Employee();
        mark.setId(10);
        mark.setName("Mark");
        mark.setSalary(6400);
        mark.setCountry("Poland");
        mark.setComments("manager comment one".toCharArray());
        mark.setPhoto(RandomUtils.nextBytes(2000));
        mark.setManager(mark);

        Employee john = new Employee();
        john.setId(11);
        john.setName("John");
        john.setSalary(2800);
        john.setCountry("Japan");
        john.setComments("john employee comment".toCharArray());
        john.setPhoto(RandomUtils.nextBytes(6000));
        john.setManager(mark);

        entityManager.persist(mark);
        entityManager.persist(john);
    }

    /*public List<Evaluation> findAllEvaluation() {
        return entityManager.createQuery("SELECT e FROM Evaluation e", Evaluation.class)
                .getResultList();
    }*/
}