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

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ManyToOneUnidirectionalJointableTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void many_to_one_unidirectional() {
        System.out.println(" *** Many-to-one unidirectional using jointable ***");
        Employee john = new Employee();
        john.setName("John");
        john.setSalary(3400);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4800);

        Employee mike = new Employee();
        mike.setName("Mike");
        mike.setSalary(6200);

        Department department = new Department();
        department.setName("HR Department");

        john.setDepartment(department);
        mark.setDepartment(department);
        mark.setDepartment(department);

        entityManager.persist(department);
        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.persist(mike);

    }


}