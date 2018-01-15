import configuration.RootConfig;
import model.Department;
import model.Employee;
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
import java.util.HashMap;
import java.util.Map;

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MapUntypedTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void map_untyped_entity_value_test() {

        Employee john = new Employee();
        john.setName("John Malkovic");
        john.setSalary(2800);

        Employee mike = new Employee();
        mike.setName("Mike vazovski");
        mike.setSalary(3900);

        Department department = new Department();
        department.setName("Human Resources");

        //add employee dodaje takze department do employee
        department.addEmployee(john);
        department.addEmployee(mike);

        entityManager.persist(john);
        entityManager.persist(mike);
        entityManager.persist(department);
    }


    @Test
    @Transactional
    @Rollback(false)
    public void map_untyped_basic_value_test() {

        Employee john = new Employee();
        john.setName("John Malkovic");
        john.setSalary(2800);

        Map<String, String> nameToPhoneNumberMap = new HashMap<String, String>();
        nameToPhoneNumberMap.put("Greg", "780 23 23 8");
        nameToPhoneNumberMap.put("Steven", "780 19 23 22");
        nameToPhoneNumberMap.put("Joe", "380 22 12 12");
        nameToPhoneNumberMap.put("Ben", "240 12 11 88");

        john.setPhoneNumbers(nameToPhoneNumberMap);

        entityManager.persist(john);
    }
}