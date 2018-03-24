import configuration.RootConfig;
import model.Address;
import model.Department;
import model.Employee;
import model.EmployeeDetails;
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
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/*

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryAdvancedConstructorResultMapping {

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        // Employees
        Employee mark = new Employee();
        mark.setId(11);
        mark.setName("Mark");
        mark.setSalary(12400);

        Address markAddress = new Address();
        markAddress.setCity("Gdanski");
        markAddress.setZip("02-120");
        markAddress.setStreet("Mlynska");
        markAddress.setState("Pomorskie");

        mark.setAddress(markAddress);
        mark.setManager(mark);


        Employee john = new Employee();
        john.setId(12);
        john.setName("John");
        john.setSalary(6400);

        Address johnAddress = new Address();
        johnAddress.setCity("Warsaw");
        johnAddress.setZip("02-495");
        johnAddress.setStreet("Krakowiakow");
        johnAddress.setState("Mazowieckie");

        john.setAddress(johnAddress);
        john.setManager(mark);

        Employee bob = new Employee();
        bob.setName("Bob");
        bob.setSalary(2800);

        Address bobAddress = new Address();
        bobAddress.setCity("Poznan");
        bobAddress.setZip("01-333");
        bobAddress.setStreet("Mniszka");
        bobAddress.setState("Wielkopolskie");

        bob.setAddress(bobAddress);
        bob.setManager(john);


        Employee mike = new Employee();
        mike.setId(13);
        mike.setName("Mike");
        mike.setSalary(3200);

        Address mikeAddress = new Address();
        mikeAddress.setCity("Krakow");
        mikeAddress.setZip("04-888");
        mikeAddress.setStreet("Smocza");
        mikeAddress.setState("Malopolskie");

        mike.setAddress(mikeAddress);
        mike.setManager(john);

        mark.addDirect(john);
        mark.addDirect(bob);
        mark.addDirect(mike);

        john.addDirect(bob);
        john.addDirect(mike);


        //Department

        Department executiveDepartment = new Department();
        executiveDepartment.setName("IT_Board");
        mark.setDepartment(executiveDepartment);
        john.setDepartment(executiveDepartment);

        Department itDepartment = new Department();
        itDepartment.setName("IT - Web subdepartment");
        bob.setDepartment(itDepartment);
        mike.setDepartment(itDepartment);


        entityManager.persist(john);
        entityManager.persist(mark);
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_advanced_constructor_result_mapping() {
        System.out.println(" *** Advanced queries - constructor result mapping***");

        System.out.println(" - Find employees with manager: ");
        for (Object result : findAllEmployeeDetails()) {
            System.out.println(result);
        }
    }

    public List<EmployeeDetails> findAllEmployeeDetails() {
        Query query = entityManager.createNativeQuery(
                "SELECT e.name, e.salary, d.name AS deptName " +
                        "FROM emp e, dept d " +
                        "WHERE e.dept_id = d.id",
                "EmployeeDetailMapping");
        return query.getResultList();
    }


}