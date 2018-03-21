import configuration.RootConfig;
import model.Address;
import model.Department;
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
import javax.persistence.Query;
import java.util.List;

/*

 */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryAdvancedSqlResultSetMapping {

    private final String INSERT_SQL = "INSERT INTO message_log (message, log_dttm) VALUES(?, CURRENT TIMESTAMP)";
    private final String DELETE_SQL = "DELETE FROM message_log";
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void prepareData() {
        Employee mark = new Employee();
        mark.setId(11);
        mark.setName("Mark");
        mark.setManager(mark);
        mark.setSalary(6400);

        Address markAddress = new Address();
        markAddress.setCity("Warsaw");
        markAddress.setState("Mazowieckie");
        markAddress.setStreet("Mazowiecka");
        markAddress.setZip("02-495");
        mark.setAddress(markAddress);

        Department executiveDepartment = new Department();
        executiveDepartment.setId(1);
        executiveDepartment.setName("Executive department");

        mark.setDepartment(executiveDepartment);


        Employee john = new Employee();
        john.setId(12);
        john.setName("John");
        john.setManager(mark);
        john.setSalary(4200);

        Address johnAddress = new Address();
        johnAddress.setCity("Krakow");
        johnAddress.setState("Malopolskie");
        johnAddress.setStreet("Stary kazimierz");
        johnAddress.setZip("03-244");
        john.setAddress(johnAddress);

        Department itDepartment = new Department();
        itDepartment.setId(2);
        itDepartment.setName("IT department");

        john.setDepartment(itDepartment);

        Employee mike = new Employee();
        mike.setId(13);
        mike.setName("Mike");
        mike.setManager(mark);
        mike.setSalary(3400);

        Address mikeAddress = new Address();
        mikeAddress.setCity("Rzeszow");
        mikeAddress.setState("Podkarpackie");
        mikeAddress.setStreet("Lwowska");
        mikeAddress.setZip("04-322");
        mike.setAddress(mikeAddress);
        mike.setDepartment(itDepartment);

        entityManager.persist(executiveDepartment);
        entityManager.persist(itDepartment);


        entityManager.persist(mark);
        entityManager.persist(john);
        entityManager.persist(mike);

        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void sql_result_set_mapping_test() {
        System.out.println(" *** Advanced queries - sql ResultSet mapping ***");

        System.out.println(" - Find all employees: ");
        for (Employee employee : findAllEmployees()) {
            System.out.println("Emp: " + employee);
            System.out.println("Addres of Emp: " + employee.getAddress());
        }

        System.out.println(" - Find employees with address: ");
        for (Object[] result : findEmployeeWithAddress()) {
            for (Object o : result) {
                System.out.println(o);
            }
        }

        System.out.println(" - Find employees with manager: ");
        for (Object result : findEmployeeWithManager()) {
            Object[] resultArray = (Object[]) result;
            for (Object o : resultArray) {
                System.out.print(" " + o + ",");
            }
            System.out.println();
        }

        System.out.println(" - Find department summary: ");
        for (Object result : findDepartmentSummary()) {
            Object[] resultArray = (Object[]) result;
            for (Object o : resultArray) {
                System.out.print(" " + o + ",");
            }
            System.out.println();
        }


    }

    public List<Employee> findAllEmployees() {
        Query query = entityManager.createNativeQuery(
                "SELECT emp_id, name, salary, manager_id, dept_id, address_id " +
                        "FROM EMP ",
                "EmployeeResult");
        return query.getResultList();
    }

    public List<Object[]> findEmployeeWithAddress() {
        Query query = entityManager.createNativeQuery(
                "SELECT emp_id, name, salary, manager_id, dept_id, address_id, " +
                        "id, street, city, state, zip " +
                        "FROM emp, address " +
                        "WHERE address_id = id",
                "EmployeeWithAddress");
        return query.getResultList();
    }

    public List findEmployeeWithManager() {
        Query query = entityManager.createNativeQuery(
                "SELECT e.name AS emp_name, m.name AS manager_name " +
                        "FROM emp e, emp m " +
                        "WHERE e.manager_id = m.emp_id",
                "EmployeeAndManager");
        return query.getResultList();
    }

    public List findDepartmentSummary() {
        Query query = entityManager.createQuery(
                "SELECT d, m, COUNT(e), AVG(e.salary) " +
                        "FROM Department d LEFT JOIN d.employees e " +
                        "LEFT JOIN d.employees m " +
                        "WHERE m IN (SELECT de.manager " +
                        "FROM Employee de " +
                        "WHERE de.department = d) " +
                        "GROUP BY d, m");
        return query.getResultList();
    }
}