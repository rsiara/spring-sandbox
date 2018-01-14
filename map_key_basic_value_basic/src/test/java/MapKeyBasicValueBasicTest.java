import configuration.RootConfig;
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
public class MapKeyBasicValueBasicTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void map_key_basic_value_basic_test() {

        Employee john = new Employee();
        john.setName("John");
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