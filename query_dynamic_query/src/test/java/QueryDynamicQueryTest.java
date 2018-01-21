import configuration.RootConfig;
import model.Department;
import model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryDynamicQueryTest {

    private static final String QUERY =
            "SELECT e.salary " +
                    "FROM Employee e " +
                    "WHERE e.department.name = :deptName AND " +
                    "      e.name = :empName ";

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    public void query_dynamic_query_test() {
        Department department = new Department();
        department.setName("Humar Resources");

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4200);

        department.addEmployee(john);
        department.addEmployee(mark);

        System.out.println("Before persist(): " + entityManager.contains(department));
        entityManager.persist(department);
        System.out.println("After persist(): " + entityManager.contains(department));
        entityManager.flush();
    }

    public long queryEmpSalary(String deptName, String empName) {
        String query = "SELECT e.salary " +
                "FROM Employee e " +
                "WHERE e.department.name = '" + deptName + "' AND " +
                "      e.name = '" + empName + "'";
        try {
            return entityManager.createQuery(query, Long.class).getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public long queryEmpSalaryUsingParams(String deptName, String empName) {
        try {
            return entityManager.createQuery(QUERY, Long.class)
                    .setParameter("deptName", deptName)
                    .setParameter("empName", empName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }


    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }


}