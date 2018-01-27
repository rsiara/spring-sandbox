import configuration.RootConfig;

import model.Department;
import model.Employee;
import model.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.method.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
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
public class QueryBulkQueryTest {


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
    public void query_executing_queries_examples_test() {

        prepare_data();
        // Constraint vialation error
        // removeDepartmentsFailure();

        // Deep path is not supported
        // clearSelectedEmployeeDepartments();

        Department department = findAllDepartments().get(0);
        Employee employee = findAllEmployees().get(0);
        assignManager(department, employee);

        List<Project> projects = findAllProjects();

        System.out.println("Before remove empty project");
        for (Project project : projects) {
            System.out.println("3. Project: " + project);
        }

        removeEmptyProjects();

        projects = findAllProjects();

        System.out.println("After remove empty project");
        for (Project project : projects) {
            System.out.println("3. Project: " + project);
        }

    }

    private void prepare_data() {
        Department departmentCA13 = new Department();
        departmentCA13.setName("CA13");

        Department departmentCA19 = new Department();
        departmentCA19.setName("CA19");

        Department departmentNY30 = new Department();
        departmentNY30.setName("NY30");

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

        Project hospitalProject = new Project();
        hospitalProject.setName("Hospital project");

        hospitalProject.setEmployees(employess);

        Project schoolProject = new Project();
        schoolProject.setName("School project");

        schoolProject.setEmployees(employess);


        Collection<Project> projects = new ArrayList<Project>();
        projects.add(hospitalProject);
        projects.add(schoolProject);

        john.setProjects(projects);
        mark.setProjects(projects);

        departmentCA13.addEmployee(john);
        departmentCA19.addEmployee(mark);
        departmentNY30.setEmployees(employess);

        entityManager.persist(departmentCA13);
        entityManager.persist(departmentCA19);
        entityManager.persist(departmentNY30);

        entityManager.persist(hrDepartmentProject);
        entityManager.persist(hospitalProject);
        entityManager.persist(schoolProject);

        entityManager.flush();
    }

    /*    DEPARTMENT SERVICE CONTENT*/

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeDepartmentsFailure() {
        entityManager.createQuery("DELETE FROM Department d " +
                "WHERE d.name IN ('CA13', 'CA19', 'NY30')")
                .executeUpdate();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void clearSelectedEmployeeDepartments() {
        entityManager.createQuery("UPDATE Employee e SET e.department = null WHERE e.department.name IN ('CA13', 'CA19', 'NY30')")
                .executeUpdate();
    }

    public Department findDepartment(int id) {
        return entityManager.find(Department.class, id);
    }

    public List<Department> findAllDepartments() {
        return entityManager.createQuery("SELECT d FROM Department d", Department.class)
                .getResultList();
    }

    /*    PROJECT SERVICE CONTENT*/


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void assignManager(Department dept, Employee manager) {
        entityManager.createQuery("UPDATE Employee e " +
                "SET e.manager = :manager " +
                "WHERE e.department = :dept ")
                .setParameter("manager", manager)
                .setParameter("dept", dept)
                .executeUpdate();
    }

    public Employee findEmployee(int id) {
        return entityManager.find(Employee.class, id);
    }

    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }

    /*    EMPLOYEE SERVICE CONTENT*/

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeEmptyProjects() {
        entityManager.createQuery("DELETE FROM Project p " +
                "WHERE p.employees IS EMPTY ")
                .executeUpdate();
    }

    public List<Project> findAllProjects() {
        return entityManager.createQuery("SELECT p FROM Project p", Project.class)
                .getResultList();
    }

}