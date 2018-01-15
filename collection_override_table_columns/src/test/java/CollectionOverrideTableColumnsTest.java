import configuration.RootConfig;
import model.Employee;
import model.VacationEntry;
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
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionOverrideTableColumnsTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void collection_override_table_columns_untyped_collection_test() {

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);

        VacationEntry vacationEntry = new VacationEntry();
        vacationEntry.setDaysTaken(21);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 5, 21);
        vacationEntry.setStartDate(startDate);

        entityManager.persist(john);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void collection_generic_basict_type_set_test() {

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);

        Set nickNamesSet = new HashSet();
        nickNamesSet.add("Alfa 0");
        nickNamesSet.add("Predator 323");
        nickNamesSet.add("Black hawk down");

        john.setNickNames(nickNamesSet);

        entityManager.persist(john);
    }


}