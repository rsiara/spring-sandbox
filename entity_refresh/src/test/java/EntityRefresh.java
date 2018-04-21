import configuration.RootConfig;
import model.Employee;
import org.junit.Before;
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

    Refresh the state of the instance from the database, overwriting changes made to the entity, if any.


 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityRefresh {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Before
    public void prepareData() {

    }

    @Test
    @Transactional
    @Rollback(false)
    public void entity_refresh() {
        System.out.println(" *** Entity refresh***");

        Employee employee = new Employee();
        employee.setName("John ");
        employee.setId(12);
        employee.setSalary(3400);

        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();


        employee = findEmployee(employee.getId());
        employee.setSalary(5000);


        entityManager.refresh(employee);

        System.out.println(employee);
    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }


}