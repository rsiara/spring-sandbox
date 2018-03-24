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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/*

 Paginacji nie mozna zaimplementowac tak po prostu,
 nalezy stworzyc logike do wykonywania stronnicowania, previous(), getCurrent(), next().

 Aby umozliwic stronnicowanie zawsze trzeba sie podepszec dwoma zapytaniami.
 - countem ktory zliczy ilosc wynikow
 - zapytaniem wlasciwym uwzgledniajacym parametry stronnicowania, ktore zwroci odpowiednie (wybrane wyniki).
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


    private String reportQueryName;
    private int currentPage;
    private int maxResults;
    private int pageSize;
    private String countQueryName;

    @Before
    public void init() {
        System.out.println("BEFORE TESTS");

        this.pageSize = 1;
        this.reportQueryName = "findAllEmployees";
        this.countQueryName = "countEmployees";
        maxResults = (entityManager.createNamedQuery(countQueryName, Long.class)
                .getSingleResult()).intValue();
        currentPage = 0;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_executing_queries_examples_test() {

        prepare_data();

        List<Employee> currentResults = getCurrentResults();
        for (Employee employee : currentResults) {
            System.out.println("1. Employee current result: " + employee);
        }

        next();

        currentResults = getCurrentResults();
        for (Employee employee : currentResults) {
            System.out.println("1. Employee current resutlt after next: " + employee);
        }

        previous();

        currentResults = getCurrentResults();
        for (Employee employee : currentResults) {
            System.out.println("1. Employee current resutlt after previous: " + employee);
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getMaxPages() {
        return maxResults / pageSize;
    }

    public List<Employee> getCurrentResults() {
        return entityManager.createNamedQuery(reportQueryName, Employee.class)
                .setFirstResult(currentPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public void next() {
        currentPage++;
    }

    public void previous() {
        currentPage--;
        if (currentPage < 0) {
            currentPage = 0;
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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