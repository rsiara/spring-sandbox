import configuration.RootConfig;
import model.ContractEmployee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
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
import java.util.Date;

/*
JPA spec (paragraph 2.12) is saying that Support for the combination of inheritance strategies
within a single entity inheritance hierarchy is not required by this specification.
Keeping that in mind, in similar cases I usually use JOINED strategy for all of my entities.

*/

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class InheritanceMixedTest {

    private EntityManager entityManager;

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void inheritance_mixed() {
        System.out.println(" *** Inheritance - mixed ***");

        ContractEmployee greg = new ContractEmployee();
        greg.setId(13);
        greg.setName("Greg");
        greg.setTerm(34);
        greg.setDailyRate(12);
        greg.setStartDate(tomorrow);

        FullTimeEmployee john = new FullTimeEmployee();
        john.setId(11);
        john.setName("John");
        john.setPensionContribution(120);
        john.setSalary(4200);
        john.setVacation(4);
        john.setStartDate(today);

        PartTimeEmployee mark = new PartTimeEmployee();
        mark.setId(12);
        mark.setName("Mark");
        mark.setHourlyRate(new Float(4.2));
        mark.setVacation(12);
        mark.setStartDate(tomorrow);

        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.persist(greg);
    }
}