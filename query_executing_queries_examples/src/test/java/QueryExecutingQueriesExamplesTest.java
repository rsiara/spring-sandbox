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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
public class QueryExecutingQueriesExamplesTest {


    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    TypedQuery<Employee> unassignedQuery;

    @Before
    public void init() {
        System.out.println("BEFORE TESTS");

        unassignedQuery =
                entityManager.createQuery("SELECT e " +
                        "FROM Employee e " +
                        "WHERE e.projects IS EMPTY", Employee.class);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_executing_queries_examples_test() {

        prepare_data();
        Department department = findAllDepartments().get(0);

        List<Employee> employeesWithoutProjects = findEmployeesWithoutProjects();
        for (Employee employee : employeesWithoutProjects) {
            System.out.println("1. Employee without project: " + employee);
        }

        List<Employee> projectEmployees = findProjectEmployees("HR Headquarter development");
        for (Employee employee : projectEmployees) {
            System.out.println("2. Employee from projet - HR Headquarter development: " + employee);
        }

        List<Employee> employees = findAllEmployees();
        System.out.println("All employee:");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public List<Employee> findEmployeesWithoutProjects() {
        return unassignedQuery.getResultList();
    }

    public List<Employee> findProjectEmployees(String projectName) {
        return entityManager.createQuery("SELECT e " +
                "FROM Project p JOIN p.employees e " +
                "WHERE p.name = :project " +
                "ORDER BY e.name", Employee.class)
                .setParameter("project", projectName)
                .getResultList();
    }


    public List<Project> findAllProjects() {
        return entityManager.createQuery("SELECT p FROM Project p", Project.class)
                .getResultList();
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