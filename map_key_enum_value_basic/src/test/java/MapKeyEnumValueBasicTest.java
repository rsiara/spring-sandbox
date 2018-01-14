import configuration.RootConfig;
import model.Employee;
import model.PhoneType;
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
public class MapKeyEnumValueBasicTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void map_key_embeddable_non_insertable_value_entity_test() {

        Employee john = new Employee();
        john.setName("John Malkovic");
        john.setSalary(2800);

        Map<PhoneType, String> phoneTypeStringMap = new HashMap<PhoneType, String>();
        phoneTypeStringMap.put(PhoneType.Home, " 48227802233");
        phoneTypeStringMap.put(PhoneType.Work, "+42890890333");
        phoneTypeStringMap.put(PhoneType.Mobile, "780122423");

        john.setPhoneNumbers(phoneTypeStringMap);

        entityManager.persist(john);
    }
}