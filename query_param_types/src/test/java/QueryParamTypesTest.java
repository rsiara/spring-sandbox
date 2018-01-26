import configuration.RootConfig;
import model.Department;
import model.Employee;
import model.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryParamTypesTest {


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
    public void query_param_types_test() {

        prepare_data();
        Department department = findAllDepartments().get(0);

        List<Employee> employeesAboveSal = findEmployeesAboveSal(department, 2700);
        for (Employee employee : employeesAboveSal) {
            System.out.println("1. Employee above salary 2700: " + employee);
        }

        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        List<Employee> employeesHiredDuringPeriod = findEmployeesHiredDuringPeriod(today, tomorrow);
        for (Employee employee : employeesHiredDuringPeriod) {
            System.out.println("2. Employee hired during period from today to tomorrow: " + employee);
        }


        Employee highestPaidByDepartment = findHighestPaidByDepartment(department);
        System.out.println("3. Highest paid by department employee: " + highestPaidByDepartment);

        List<Employee> employees = findAllEmployees();
        System.out.println("All employee:");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public List<Employee> findEmployeesAboveSal(Department dept, long minSal) {
        return entityManager.createNamedQuery("findEmployeesAboveSal", Employee.class)
                .setParameter("dept", dept)
                .setParameter("sal", minSal)
                .getResultList();
    }

    public List<Employee> findEmployeesHiredDuringPeriod(Date start, Date end) {
        return entityManager.createQuery("SELECT e " +
                "FROM Employee e " +
                "WHERE e.startDate BETWEEN :start AND :end", Employee.class)
                .setParameter("start", start, TemporalType.DATE)
                .setParameter("end", end, TemporalType.DATE)
                .getResultList();
    }

    public Employee findHighestPaidByDepartment(Department dept) {
        try {
            return entityManager.createNamedQuery("findHighestPaidByDepartment", Employee.class)
                    .setParameter("dept", dept)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    public List<Department> findAllDepartments() {
        return entityManager.createQuery("SELECT d FROM Department d", Department.class).getResultList();
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