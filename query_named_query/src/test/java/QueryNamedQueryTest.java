import configuration.RootConfig;
import model.Department;
import model.Employee;
import model.Project;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryNamedQueryTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_named_query_test() {

        prepare_data();

        Employee employeeByName = findEmployeeByName("John");
        System.out.println("Employee by name (John): " + employeeByName);

        Employee employeeById = findEmployeeByPrimaryKey(employeeByName.getId());
        System.out.println("Employee by id  of (John): " + employeeByName);

        long salaryOfEmployee = findSalaryForNameAndDepartment("Humar Resources", "John");
        System.out.println("Salary of Employee (John): " + salaryOfEmployee);

        List<Employee> employees = findAllEmployees();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public Employee findEmployeeByName(String name) {
        try {
            return entityManager.createNamedQuery("Employee.findByName", Employee.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Employee findEmployeeByPrimaryKey(int id) {
        try {
            return entityManager.createNamedQuery("Employee.findByPrimaryKey", Employee.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long findSalaryForNameAndDepartment(String deptName, String empName) {
        try {
            return entityManager.createNamedQuery("findSalaryForNameAndDepartment", Long.class)
                    .setParameter("deptName", deptName)
                    .setParameter("empName", empName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0;
        }
    }

    public List<Employee> findAllEmployees() {
        return entityManager.createNamedQuery("Employee.findAll", Employee.class)
                .getResultList();
    }


    private void prepare_data() {
        Department department = new Department();
        department.setName("Humar Resources");

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);
        john.setStartDate(new Date());

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4200);
        mark.setStartDate(new Date());

        Collection<Employee> employess = new ArrayList<Employee>();
        employess.add(john);
        employess.add(mark);

        john.setDirects(employess);
        mark.setManager(john);
        john.setManager(john);

        Project hrDepartmentProject = new Project();
        hrDepartmentProject.setName("HR Headquarter development");

        hrDepartmentProject.setEmployees(employess);


        Project hospitalProject = new Project();
        hospitalProject.setName("Hospital project");

        hospitalProject.setEmployees(employess);

        Project schoolProject = new Project();
        schoolProject.setName("School project");

        schoolProject.setEmployees(employess);


        Collection<Project> projects = new ArrayList<Project>();
        projects.add(hrDepartmentProject);
        projects.add(hospitalProject);
        projects.add(schoolProject);

        john.setProjects(projects);
        mark.setProjects(projects);

        department.addEmployee(john);
        department.addEmployee(mark);

        entityManager.persist(department);

        entityManager.persist(hrDepartmentProject);
        entityManager.persist(hospitalProject);
        entityManager.persist(schoolProject);
    }

}