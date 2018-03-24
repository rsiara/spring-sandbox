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

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MapKeyEmbeddableAttrOverrideValueEntityTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void map_key_embeddable_attr_override_value_entity_test() {

        Employee john = new Employee();
        john.setFirstName("John");
        john.setLastName("Malkovic");
        john.setSalary(2800);

        Employee mark = new Employee();
        mark.setFirstName("Mark");
        mark.setLastName("Jonkovic");
        mark.setSalary(3800);

        Department humanResource = new Department();
        humanResource.setName("Human Resource");
        humanResource.addEmployee(john);
        humanResource.addEmployee(mark);

        Department it = new Department();
        it.setName("Information Technology");
        it.addEmployee(john);

        entityManager.persist(john);
        entityManager.persist(mark);

        entityManager.persist(humanResource);
        entityManager.persist(it);
    }
}