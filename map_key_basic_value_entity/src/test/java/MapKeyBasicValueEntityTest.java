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
public class MapKeyBasicValueEntityTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void map_key_basic_value_entity_test() {

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(2800);

        Department department = new Department();
        department.setName("Human Resources");

        department.addEmployee("240B", john);
        department.addEmployee("220A", mark);


        john.setDepartment(department);
        entityManager.persist(john);
        mark.setDepartment(department);
        entityManager.persist(mark);

        entityManager.persist(department);
    }
}