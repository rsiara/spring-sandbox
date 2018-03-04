import configuration.RootConfig;
import model.Employee;
import model.EmployeeId;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

/*

 ***   COMPOUND PRIMARY KEY - EMBEDDED CLASS ID   ***
 *

 *
 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmbeddedClassIdTest {

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
    public void compound_primary_keys_embedded_class_id() {
        System.out.println(" *** Compound primary key - class id *** ");

        Integer id = 24;
        String name = "John";
        Integer salary = 2800;
        String country = "Poland";

        //Employee

        Employee employee = new Employee(country, id);
        employee.setName(name);
        employee.setSalary(salary);

        entityManager.persist(employee);

        // Find by compound ID

        Employee retrievedEmployee = findEmployeeById(country, id);

        System.out.println(retrievedEmployee);

        Employee retrievedEmployeeSecond = findEmployee(country, id);

        System.out.println(retrievedEmployee);
    }

    private Employee findEmployeeById(String country, int id) {
        return entityManager.find(Employee.class, new EmployeeId(country, id));
    }

    public Employee findEmployee(String country, int id) {
        if (country == null || StringUtils.isBlank(country)) {
            return null;
        }
        try {
            return (Employee)
                    entityManager.createQuery("SELECT e " +
                            "FROM Employee e " +
                            "WHERE e.id.country = ?1 AND e.id.id = ?2")
                            .setParameter(1, country)
                            .setParameter(2, id)
                            .getSingleResult();
        } catch (NoResultException nrEx) {
            return null;
        }
    }
}