import configuration.RootConfig;
import model.Employee;
import model.EmployeeHistory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

/*

 ***   DERIVED SHARED MAPS ID  ***
 *
 *  Allow to keep FK from relation as Primary KEY and duplicate it to separate id attribute
 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class IdDerivedSharedMapsIdTest {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    Date todayplustwo = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 2);
    Date todayplustree = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 3);
    Date todayplusfour = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 4);
    Date todayplusfive = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 5);
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void init() {
        System.out.println("BEFORE TESTS");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void derived_shared_maps_id() {
        System.out.println(" *** Derived shared maps id *** ");

        Integer id = 24;
        String name = "John";
        Integer salary = 2800;
        String country = "Poland";

        //Employee

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSalary(salary);

        EmployeeHistory employeeHistory = new EmployeeHistory(employee);

        entityManager.persist(employee);
        entityManager.persist(employeeHistory);

        entityManager.flush();

        EmployeeHistory retrievedEmployeeHistory = findEmployeeHistoryById(employee.getId());

        System.out.println(retrievedEmployeeHistory.getId());
    }


    private EmployeeHistory findEmployeeHistoryById(int id) {
        return entityManager.find(EmployeeHistory.class, id);
    }
}