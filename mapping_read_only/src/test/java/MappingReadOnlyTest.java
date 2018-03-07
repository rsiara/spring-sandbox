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
import java.util.Collections;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingReadOnlyTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void mapping_read_only_test() {
        Employee john = new Employee();
        john.setName("John");
        john.setSalary(3400);

        Department department = new Department();
        department.setName("HR Department");
        department.setEmployees(Collections.singletonList(john));
        department.setId(24);

        john.setDepartment(department);

        entityManager.persist(department);
        entityManager.persist(john);

    }


}