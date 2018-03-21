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


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class InheritanceJoinedTableTest {

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
    public void inheritance_joined_table_test() {
        System.out.println(" *** Inheritance - joined table ***");

        FullTimeEmployee john = new FullTimeEmployee();
        john.setId(11);
        john.setName("John");
        john.setPension(2800);
        john.setSalary(4200);
        john.setVacation(4);
        john.setStartDate(today);

        PartTimeEmployee mark = new PartTimeEmployee();
        mark.setId(12);
        mark.setName("Mark");
        mark.setHourlyRate(new Float(4.2));
        mark.setVacation(12);
        mark.setStartDate(tomorrow);

        ContractEmployee greg = new ContractEmployee();
        greg.setId(13);
        greg.setName("Greg");
        greg.setTerm(34);
        greg.setDailyRate(12);
        greg.setStartDate(tomorrow);

        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.persist(greg);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void dump_test() {
        System.out.println(" *** Dump test ***");

        String stringToReplace = " wor12d prefix otherWord   afterTwoSpaceWord";

        stringToReplace = stringToReplace.replaceAll("(?<=\\s|^)(?=\\S)", "ux-");
        System.out.println(stringToReplace);

        stringToReplace = stringToReplace.replaceAll("ux-", org.apache.commons.lang3.StringUtils.EMPTY);
        System.out.println(stringToReplace);
    }
}